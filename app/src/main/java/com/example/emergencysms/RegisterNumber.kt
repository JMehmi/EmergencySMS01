package com.example.emergencysms

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterNumberBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val sharedP = getSharedPreferences("sharedPref", MODE_PRIVATE)
        val editor = sharedP.edit()

        binding.saveNumber.setOnClickListener() {
                val phnNumber= binding.numberEdit.text.toString()
                if(phnNumber.isNotEmpty() && phnNumber.length==12) {
                   // editor.putString("ENUM",phnNumber)
                    //editor.commit()
                    val intent = Intent (this, MainActivity::class.java)
                        intent.putExtra("phnNumber",phnNumber)
                    startActivity(intent)
                }else{
                    Toast.makeText(this, "Enter phone number with country code", Toast.LENGTH_SHORT).show()
                }
        }



        }

    }
