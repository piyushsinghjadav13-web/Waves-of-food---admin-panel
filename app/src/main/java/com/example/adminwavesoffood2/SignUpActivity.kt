package com.example.adminwavesoffood2

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.adminwavesoffood2.databinding.ActivitySignUpBinding
import com.example.adminwavesoffood2.model.usermodel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class SignUpActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    private val binding: ActivitySignUpBinding by lazy {
        ActivitySignUpBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.createaccount.setOnClickListener {

            val userName = binding.Name.text.toString().trim()
            val restaurantName = binding.Restaurant.text.toString().trim()
            val email = binding.emailphone.text.toString().trim()
            val password = binding.password.text.toString().trim()

            if (userName.isEmpty() || restaurantName.isEmpty() ||
                email.isEmpty() || password.isEmpty()
            ) {
                Toast.makeText(this, "Please fill all details", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password.length < 6) {
                Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            createAccount(userName, restaurantName, email, password)
        }

        binding.donthaveaccountbtn.setOnClickListener {
            startActivity(Intent(this, Login_Activity::class.java))
        }

        val locationList = arrayOf("Jaipur", "Odisha", "Bundi", "Sikar")
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, locationList)
        binding.Listoflocation.setAdapter(adapter)
    }

    private fun createAccount(name: String, restaurant: String, email: String, password: String) {

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->

                if (task.isSuccessful) {

                    val uid = auth.currentUser!!.uid

                    val user = usermodel(
                        name,
                        restaurant,
                        email,
                        password
                    )

                    FirebaseDatabase.getInstance().reference
                        .child("user")
                        .child(uid)
                        .setValue(user)

                    Toast.makeText(this, "Account Created Successfully", Toast.LENGTH_SHORT).show()

                    startActivity(Intent(this, Login_Activity::class.java))
                    finish()

                } else {

                    Toast.makeText(
                        this,
                        "Signup Failed: ${task.exception?.localizedMessage}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }
}
