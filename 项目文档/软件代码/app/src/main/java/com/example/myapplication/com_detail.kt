package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class com_detail : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_com_detail)
        supportActionBar?.hide()
    }
}