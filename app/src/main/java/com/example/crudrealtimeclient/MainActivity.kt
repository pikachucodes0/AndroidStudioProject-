package com.example.crudrealtimeclient

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.crudrealtimeclient.databinding.ActivityMainBinding
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Button to search for student data
        binding.searchButton.setOnClickListener {
            val searchId = binding.searchId.text.toString().trim()
            if (searchId.isNotEmpty()) {
                readData(searchId)
            } else {
                Toast.makeText(this, "Please enter ID number", Toast.LENGTH_SHORT).show()
            }
        }

        // Button to add student to scholarship
        binding.addToScholarshipButton.setOnClickListener {
            val studentId = binding.searchId.text.toString().trim()
            if (studentId.isNotEmpty()) {
                addToScholarship(studentId)
            } else {
                Toast.makeText(this, "Please enter a valid student ID", Toast.LENGTH_SHORT).show()
            }
        }

        // Button to view the scholarship list
        binding.viewScholarshipListButton.setOnClickListener {
            val intent = Intent(this, ScholarshipStudentActivity::class.java)
            startActivity(intent)
        }
    }

    // Function to fetch student data
    private fun readData(studentId: String) {
        databaseReference = FirebaseDatabase.getInstance().getReference("Student Information")
        databaseReference.child(studentId).get().addOnSuccessListener {
            if (it.exists()) {
                val studentName = it.child("studentName").value.toString()
                val studentClass = it.child("studentClass").value.toString()

                // Display fetched data
                binding.readName.text = studentName
                binding.readClass.text = studentClass
                binding.readId.text = studentId

                Toast.makeText(this, "Results Found", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Student ID does not exist", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
        }
    }

    // Function to add student to the scholarship "table"
    private fun addToScholarship(studentId: String) {
        if (studentId.isEmpty()) {
            Toast.makeText(this, "Please enter a valid student ID", Toast.LENGTH_SHORT).show()
            return
        }

        // Reference to the "Student Information" node to fetch the student data
        val studentRef = FirebaseDatabase.getInstance().getReference("Student Information").child(studentId)

        studentRef.get().addOnSuccessListener {
            if (it.exists()) {
                // Get student data
                val studentName = it.child("studentName").value.toString()
                val studentClass = it.child("studentClass").value.toString()

                // Reference to the "Scholarship" node to add the student
                val scholarshipRef = FirebaseDatabase.getInstance().getReference("Scholarship")

                // Creating a map with the student details
                val scholarshipStudent = mapOf(
                    "studentName" to studentName,
                    "studentId" to studentId,
                    "studentClass" to studentClass
                )

                // Adding student to the "Scholarship" node, using the studentId as the key
                scholarshipRef.child(studentId).setValue(scholarshipStudent).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Student added to scholarship", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Failed to add student to scholarship", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Student ID does not exist", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Error fetching student data", Toast.LENGTH_SHORT).show()
        }
    }
}
