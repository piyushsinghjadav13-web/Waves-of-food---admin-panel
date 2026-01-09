package com.example.adminwavesoffood2

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.adminwavesoffood2.databinding.ActivityMainBinding
import com.example.adminwavesoffood2.model.orderDetails
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private lateinit var database: FirebaseDatabase
    private lateinit var auth: FirebaseAuth
    private lateinit var completeOrderReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // ⭐ Initialize Firebase
        database = FirebaseDatabase.getInstance()
        auth = FirebaseAuth.getInstance()

        setupClickListeners()
        pendingOrders()
        completedOrders()
        wholeTimeEarning()
    }



    private fun setupClickListeners() {
        binding.additem.setOnClickListener {
            startActivity(Intent(this, AdditemActivity2::class.java))
        }

        binding.allitemmenu.setOnClickListener {
            startActivity(Intent(this, AllMenueActivity2::class.java))
        }

        binding.dispatch.setOnClickListener {
            startActivity(Intent(this, outfordeleveryActivity::class.java))
        }

        binding.profile.setOnClickListener {
            startActivity(Intent(this, AdminProfile::class.java))
        }

        binding.newuser.setOnClickListener {
            startActivity(Intent(this, Createuser::class.java))
        }

        binding.pendingimg.setOnClickListener {
            startActivity(Intent(this, Pendingorder::class.java))
        }
        binding.logout.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, SignUpActivity::class.java))
            finish()
        }
    }

    // ⭐ Show Pending Order Count
    private fun pendingOrders() {
        val pendingRef = database.reference.child("orderDetails")

        pendingRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val count = snapshot.childrenCount.toInt()
                binding.pendingorder.text = count.toString()
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    // ⭐ Show Completed Order Count (dispatchOrders)
    private fun completedOrders() {
        val completeRef = database.reference.child("dispatchOrders")

        completeRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val count = snapshot.childrenCount.toInt()
                binding.ccompleteorder.text = count.toString()
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    // ⭐ Total earning calculation
    private fun wholeTimeEarning() {
        val listOfTotalPay = mutableListOf<Int>()

        completeOrderReference = database.reference.child("dispatchOrders")

        completeOrderReference.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                for (orderSnapshot in snapshot.children) {

                    val order = orderSnapshot.getValue(orderDetails::class.java)

                    // ⭐ FIX: Handle ANY datatype safely
                    val priceInt = when (val price = order?.totalPrice) {

                        is String -> price.replace("₹", "").toIntOrNull()
                        is Long -> price.toInt()
                        is Int -> price
                        else -> null
                    }

                    priceInt?.let { listOfTotalPay.add(it) }
                }

                // ⭐ Show total earnings
                binding.totalPrice.text = listOfTotalPay.sum().toString() + "₹"
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

}
