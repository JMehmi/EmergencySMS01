package com.example.emergencysms

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.emergencysms.databinding.ActivityMainBinding
import com.example.emergencysms.databinding.ActivityRegisterNumberBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        binding = ActivityMainBinding.inflate(layoutInflater)
        var phnNumber: String? = intent.getStringExtra("phnNumber")

        binding.textNum.setText(phnNumber)




    }
}