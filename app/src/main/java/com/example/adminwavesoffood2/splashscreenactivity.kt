package com.example.adminwavesoffood2

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class splashscreenactivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splashscreenactivity)

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, Login_Activity::class.java)
            startActivity(intent)
            finish() // splash screen close karne ke liye
        }, 3000)

    }
}
