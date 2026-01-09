package com.example.adminwavesoffood2

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.adminwavesoffood2.databinding.ActivityCreateuserBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class Createuser : AppCompatActivity() {

    private val binding: ActivityCreateuserBinding by lazy {
        ActivityCreateuserBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.backbtn.setOnClickListener {
            finish()
        }

        binding.loginBtn.setOnClickListener {
            addEmployee()
        }
    }

    private fun addEmployee() {

        val name = binding.Name.text.toString().trim()
        val email = binding.emailphone.text.toString().trim()
        val password = binding.password.text.toString().trim()

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        if (password.length < 6) {
            Toast.makeText(this, "Password must be 6+ characters", Toast.LENGTH_SHORT).show()
            return
        }

        // Create Employee in Firebase Authentication
        FirebaseAuth.getInstance()
            .createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->

                if (task.isSuccessful) {

                    val uid = task.result!!.user!!.uid

                    // Save employee data in database
                    val employee = HashMap<String, Any>()
                    employee["name"] = name
                    employee["email"] = email
                    employee["password"] = password

                    FirebaseDatabase.getInstance().reference
                        .child("user")
                        .child(uid)
                        .setValue(employee)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Employee Added Successfully", Toast.LENGTH_SHORT)
                                .show()
                            finish()
                        }

                } else {
                    Toast.makeText(
                        this,
                        "Failed: ${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
}
