package com.example.crudrealtimeadmin

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.crudrealtimeadmin.databinding.ActivitySignupBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Reference "admins" table instead of "users"
        databaseReference = FirebaseDatabase.getInstance().getReference("admins")

        binding.signupButton.setOnClickListener {
            registerAdmin()
        }

        binding.loginRedirectText.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    private fun registerAdmin() {
        val email = binding.signupEmail.text.toString().trim()
        val username = binding.signupUsername.text.toString().trim()
        val password = binding.signupPassword.text.toString().trim()

        if (email.isEmpty() || username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val admin = mapOf(
            "email" to email,
            "username" to username,
            "password" to password
        )

        databaseReference.child(username).setValue(admin).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Admin registered successfully!", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "Signup failed. Try again!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
