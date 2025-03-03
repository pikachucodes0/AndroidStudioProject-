package com.example.crudrealtimeadmin

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.crudrealtimeadmin.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Check if the user is logged in
        val sharedPreferences = getSharedPreferences("AdminPrefs", MODE_PRIVATE)
        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)

        if (!isLoggedIn) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        binding.mainUpload.setOnClickListener {
            startActivity(Intent(this@MainActivity, UploadActivity::class.java))
        }

        binding.mainUpdate.setOnClickListener {
            startActivity(Intent(this@MainActivity, UpdateActivity::class.java))
        }

        binding.mainDelete.setOnClickListener {
            startActivity(Intent(this@MainActivity, DeleteActivity::class.java))
        }
    }
}
