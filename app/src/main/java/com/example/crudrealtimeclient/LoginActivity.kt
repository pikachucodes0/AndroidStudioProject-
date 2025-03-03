package com.example.crudrealtimeclient

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.crudrealtimeclient.databinding.ActivityLoginBinding
import com.google.firebase.database.*

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        databaseReference = FirebaseDatabase.getInstance().getReference("users")

        binding.loginButton.setOnClickListener {
            val username = binding.loginEmail.text.toString().trim()
            val password = binding.loginPassword.text.toString().trim()

            if (username.isNotEmpty() && password.isNotEmpty()) {
                checkUser(username, password)
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }

        binding.signupRedirectText.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }
    }

    private fun checkUser(username: String, password: String) {
        databaseReference.orderByChild("username").equalTo(username)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (userSnapshot in snapshot.children) {
                            val passwordFromDB = userSnapshot.child("password").getValue(String::class.java)
                            if (passwordFromDB == password) {
                                val emailFromDB = userSnapshot.child("email").getValue(String::class.java)

                                val sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
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
                        }
                    } else {
                        binding.loginEmail.error = "User does not exist"
                        binding.loginEmail.requestFocus()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@LoginActivity, "Database error: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }
}
