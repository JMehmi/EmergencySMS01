package com.example.emergencysms

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.telephony.SmsManager
import android.widget.Button
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.ContextCompat
import com.example.emergencysms.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import java.lang.Math.sqrt
import java.util.*


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var phnNumber: String
    private lateinit var lm: LocationManager
    private var latitude: Double? =null
    private var longitude: Double? =null
    private lateinit var _linear_layout: LinearLayout



    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)




        //LOCATION
        lm = getSystemService(LOCATION_SERVICE) as LocationManager

        phnNumber =
            getSharedPreferences("sharedPref", MODE_PRIVATE).getString("Enum", "0").toString()

        if (phnNumber.equals("0")) {
            val intent = Intent(this, RegisterNumber::class.java)
            startActivity(intent)
        } else {
            binding.textNum.setText("SOS to\n $phnNumber")
        }

        val popupMbt: Button = binding.popUp

        popupMbt.setOnClickListener() {
            val popmenu: PopupMenu = PopupMenu(this, popupMbt)
            popmenu.menuInflater.inflate(R.menu.popup, popmenu.menu)
            popmenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.chngNumber ->
                        switchAct()
                }
                true
            })
            popmenu.show()
        }

        binding.apply {


            start.setOnClickListener {
                Toast.makeText(applicationContext,"Service started",Toast.LENGTH_SHORT).show()
                startService(Intent(this@MainActivity, EmService::class.java))
            }

            stop.setOnClickListener() {

                stopService(Intent(this@MainActivity, EmService::class.java))

            }
        }

    }

    private fun switchAct() {
        val intent = Intent (this, RegisterNumber::class.java)
        startActivity(intent)
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



}


