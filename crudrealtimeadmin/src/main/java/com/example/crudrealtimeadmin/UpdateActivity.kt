package com.example.crudrealtimeadmin

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.crudrealtimeadmin.databinding.ActivityUpdateBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class UpdateActivity : AppCompatActivity() {
    private lateinit var binding : ActivityUpdateBinding
    private lateinit var databaseReference: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.updateButton.setOnClickListener {
            val referenceStudentId = binding.referenceStudentId.text.toString()
            val updateStudentName = binding.updateStudentName.text.toString()
            val updateStudentClass = binding.updateStudentClass.text.toString()

            updateData(referenceStudentId,updateStudentName,updateStudentClass)
        }
    }
    private fun updateData(studentId: String, studentName: String, studentClass: String) {
        databaseReference = FirebaseDatabase.getInstance().getReference("Student Information")
        val studentData = mapOf<String,String>(
            "studentId" to studentId,
            "studentName" to studentName,
            "studentClass" to studentClass
        )
        databaseReference.child(studentId).updateChildren(studentData).addOnSuccessListener {
            binding.referenceStudentId.text.clear()
            binding.updateStudentName.text.clear()
            binding.updateStudentClass.text.clear()
            Toast.makeText(this,"Successfully Updated",Toast.LENGTH_SHORT).show()
        }.addOnFailureListener{
            Toast.makeText(this,"Failed to Update",Toast.LENGTH_SHORT).show()
        }}
}