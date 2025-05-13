package com.example.foodorder.activity

import android.content.Intent
import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity

import com.example.foodorder.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
private val binding : ActivityRegisterBinding by lazy {
    ActivityRegisterBinding.inflate(layoutInflater)
}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.tvLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }


    }


    }
