package com.example.emergencysms

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.util.Log
import android.widget.Toast
import com.example.emergencysms.databinding.ActivityMainBinding
import com.example.emergencysms.databinding.ActivityRegisterNumberBinding

class RegisterNumber : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterNumberBinding

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterNumberBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var sharedP = getSharedPreferences("sharedPref", MODE_PRIVATE)
        var num=sharedP.getString("Enum","0")


        binding.saveNumber.setOnClickListener() {
                val phnNumber= binding.numberEdit.text.toString()
                if(phnNumber.isNotEmpty() && phnNumber.length==12) {
                    var editor: SharedPreferences.Editor = sharedP.edit()
                        editor.putString("Enum",phnNumber)
                        editor.apply()
                    val intent = Intent (this, MainActivity::class.java)
                    startActivity(intent)
                }else{
                    Toast.makeText(this, "Enter phone number with country code", Toast.LENGTH_SHORT).show()
                }
        }



        }

    }
