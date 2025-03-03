package com.example.crudrealtimeclient

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase

class ScholarshipAdapter(
    private var students: MutableList<ScholarshipStudentModel>
) : RecyclerView.Adapter<ScholarshipAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_scholarship_student, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val student = students[position]
        holder.bind(student)

        holder.deleteButton.setOnClickListener {
            deleteStudent(student.studentId, position)
        }
    }

    override fun getItemCount(): Int = students.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.studentNameTextView)
        private val classTextView: TextView = itemView.findViewById(R.id.studentClassTextView)
        val deleteButton: Button = itemView.findViewById(R.id.deleteButton)

        fun bind(student: ScholarshipStudentModel) {
            nameTextView.text = student.studentName
            classTextView.text = "Class: ${student.studentClass}"
        }
    }

    private fun deleteStudent(studentId: String, position: Int) {
        val databaseRef = FirebaseDatabase.getInstance().getReference("Scholarship").child(studentId)

        databaseRef.removeValue().addOnSuccessListener {
            students.removeAt(position)
            notifyItemRemoved(position)
        }
    }
}
