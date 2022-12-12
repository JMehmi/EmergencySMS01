package com.example.emergencysms

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
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
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import java.lang.Math.sqrt
import java.util.*


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var phnNumber: String
    private lateinit var lm: LocationManager
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    private lateinit var _linear_layout: LinearLayout
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var sharedP : SharedPreferences




    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        sharedP = getSharedPreferences("sharedPref", MODE_PRIVATE)
        var lat=sharedP.getString("lat","1")
        var long=sharedP.getString("long","2")

        //LOCATION
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        phnNumber =
            getSharedPreferences("sharedPref", MODE_PRIVATE).getString("Enum", "0").toString()

        if (phnNumber.equals("0")) {
            switchAct()
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
                getLocation()
                Toast.makeText(applicationContext,"$latitude - $longitude ",Toast.LENGTH_SHORT).show()
                startService(Intent(this@MainActivity, EmService::class.java))
            }

            stop.setOnClickListener() {

                stopService(Intent(this@MainActivity, EmService::class.java))

            }
        }

    }

    private fun getLocation() {
      if(ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
          ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),100)
            return
        }
        val location= fusedLocationProviderClient.lastLocation
        location.addOnSuccessListener {
            if(it!=null){
                latitude=it.latitude
                longitude=it.longitude
                val lat= latitude.toString()
                val long=longitude.toString()
                var editor: SharedPreferences.Editor = sharedP.edit()
                editor.putString("lat",lat)
                editor.putString("long",long)
                editor.apply()
            }
        }
    }

    private fun switchAct() {
        val intent = Intent (this, RegisterNumber::class.java)
        startActivity(intent)
    }

}



