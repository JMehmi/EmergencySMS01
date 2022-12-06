package com.example.emergencysms

import android.app.Activity
import android.content.ContentProviderClient
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import android.location.LocationManager
import android.location.LocationProvider
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.SmsManager
import android.view.View
import android.widget.Button
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.getSystemService
import androidx.core.location.LocationManagerCompat.isLocationEnabled
import androidx.lifecycle.DEFAULT_ARGS_KEY
import com.example.emergencysms.databinding.ActivityMainBinding
import com.example.emergencysms.databinding.ActivityRegisterNumberBinding
import java.lang.Math.sqrt
import java.security.spec.PSSParameterSpec.DEFAULT
import java.util.*
import java.util.jar.Manifest

class MainActivity : AppCompatActivity() {
    private var sensorManager: SensorManager? = null
    private var acceleration = 0f
    private var currentAcceleration = 0f
    private var lastAcceleration = 0f
    private lateinit var binding: ActivityMainBinding
    private lateinit var phnNumber: String
    private var mex : String="SOS help me I'm here"


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //ACCELEROMETER DATA
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        Objects.requireNonNull(sensorManager)!!.registerListener(
            sensorListener, sensorManager!!
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL
        )
        acceleration = 10f
        currentAcceleration = SensorManager.GRAVITY_EARTH
        lastAcceleration = SensorManager.GRAVITY_EARTH


        binding = ActivityMainBinding.inflate(layoutInflater)

        //phnNumber= getSharedPreferences("ENUM", MODE_PRIVATE).getString("ENUM","0").toString()

        phnNumber= intent.getStringExtra("phnNumber").toString()
        if(phnNumber.equals("null")){
            val intent = Intent (this, RegisterNumber::class.java)
            startActivity(intent)
        }
        else{
            Toast.makeText(this, phnNumber, Toast.LENGTH_SHORT).show()
            //binding.textNum.setText("SOS to\n"+phnNumber)
        }

        val popupMbt: Button = binding.popUp

        popupMbt.setOnClickListener(){
            val popmenu: PopupMenu = PopupMenu(this,popupMbt)
            popmenu.menuInflater.inflate(R.menu.popup,popmenu.menu)


            popmenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.chngNumber->
                        switchAct()
                }
                true
            })
            popmenu.show()
        }

        binding.start.setOnClickListener(){
            Toast.makeText(this, "Service Started", Toast.LENGTH_SHORT).show()
            sendSMS(mex,phnNumber)
        }

    }

    private fun switchAct() {
        val intent = Intent (this, RegisterNumber::class.java)
        startActivity(intent)
    }


    private val sensorListener: SensorEventListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]
            lastAcceleration = currentAcceleration
            currentAcceleration = sqrt((x * x + y * y + z * z).toDouble()).toFloat()
            val delta: Float = currentAcceleration - lastAcceleration
            acceleration = acceleration * 0.9f + delta
            if (acceleration > 12) {

                Toast.makeText(this@MainActivity, "Shake Detected", Toast.LENGTH_SHORT).show()
            }
        }
        override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}
    }


    private fun sendSMS(s: String, phnNumber: String) {

        var smsManager: SmsManager =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                applicationContext.getSystemService<SmsManager>(SmsManager::class.java)
            } else {
                TODO("VERSION.SDK_INT < M")
            }
        smsManager.sendTextMessage("00$phnNumber",null,"SOS",null,null)
    }

    override fun onResume() {
        sensorManager?.registerListener(sensorListener, sensorManager!!.getDefaultSensor(
            Sensor .TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL
        )
        super.onResume()
    }
    override fun onPause() {
        sensorManager!!.unregisterListener(sensorListener)
        super.onPause()
    }


}
