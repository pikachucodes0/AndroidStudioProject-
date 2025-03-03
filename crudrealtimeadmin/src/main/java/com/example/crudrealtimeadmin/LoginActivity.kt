package com.example.crudrealtimeadmin

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.crudrealtimeadmin.databinding.ActivityLoginBinding
import com.google.firebase.database.*

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Check if the admin is already logged in
        val sharedPreferences = getSharedPreferences("AdminPrefs", MODE_PRIVATE)
        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)

//        if (isLoggedIn) {
//            startActivity(Intent(this, MainActivity::class.java))
//            finish()
//            return
//        }
        // Reference "admins" table instead of "users"
        databaseReference = FirebaseDatabase.getInstance().getReference("admins")

        binding.loginButton.setOnClickListener {
            val username = binding.loginEmail.text.toString().trim()
            val password = binding.loginPassword.text.toString().trim()

            if (username.isNotEmpty() && password.isNotEmpty()) {
                checkAdmin(username, password)
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }

        binding.signupRedirectText.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }
    }

    private fun checkAdmin(username: String, password: String) {
        databaseReference.child(username).get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                val passwordFromDB = snapshot.child("password").getValue(String::class.java)
                if (passwordFromDB == password) {
                    val emailFromDB = snapshot.child("email").getValue(String::class.java)

                    val sharedPreferences = getSharedPreferences("AdminPrefs", MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    editor.putBoolean("isLoggedIn", true)
                    editor.apply()

                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    intent.putExtra("email", emailFromDB)
                    intent.putExtra("username", username)
                    startActivity(intent)
                    finish()
                } else {
                    binding.loginPassword.error = "Invalid Credentials"
                    binding.loginPassword.requestFocus()
                }
            } else {
                binding.loginEmail.error = "Admin does not exist"
                binding.loginEmail.requestFocus()
            }
        }.addOnFailureListener {
            Toast.makeText(this@LoginActivity, "Database error: ${it.message}", Toast.LENGTH_SHORT).show()
        }
    }
}
