package com.example.crudrealtimeclient

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class ScholarshipStudentActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var scholarshipAdapter: ScholarshipAdapter
    private lateinit var databaseRef: DatabaseReference
    private var studentList = mutableListOf<ScholarshipStudentModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scholarship_student)

        // Fix: Use findViewById directly to get the button
        val backToMainButton = findViewById<Button>(R.id.backToMainButton)
        backToMainButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish() // Close current activity
        }

        // RecyclerView setup
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        scholarshipAdapter = ScholarshipAdapter(studentList)
        recyclerView.adapter = scholarshipAdapter

        // Firebase setup
        databaseRef = FirebaseDatabase.getInstance().getReference("Scholarship")
        fetchScholarshipStudents()
    }

    private fun fetchScholarshipStudents() {
        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                studentList.clear()
                for (childSnapshot in snapshot.children) {
                    val student = childSnapshot.getValue(ScholarshipStudentModel::class.java)
                    student?.let { studentList.add(it) }
                }
                scholarshipAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ScholarshipStudentActivity, "Failed to load data", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
