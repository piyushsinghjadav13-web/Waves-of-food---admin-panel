package com.example.adminwavesoffood2

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adminwavesoffood2.adapter.deleveryadapter
import com.example.adminwavesoffood2.databinding.ActivityOutfordeleveryBinding
import com.example.adminwavesoffood2.model.orderDetails
import com.google.firebase.database.*

class outfordeleveryActivity : AppCompatActivity() {

    private val binding: ActivityOutfordeleveryBinding by lazy {
        ActivityOutfordeleveryBinding.inflate(layoutInflater)
    }

    private lateinit var database: FirebaseDatabase
    private var completeOrdersList: ArrayList<orderDetails> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        binding.backbtn.setOnClickListener { finish() }

        loadDispatchOrders()
    }

    private fun loadDispatchOrders() {

        database = FirebaseDatabase.getInstance()

        val ref = database.reference
            .child("dispatchOrders")          // ⭐ dispatched orders node
            .orderByChild("currentTime")      // ⭐ correct sorting key

        ref.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                completeOrdersList.clear()

                for (item in snapshot.children) {
                    val order = item.getValue(orderDetails::class.java)
                    if (order != null) completeOrdersList.add(order)
                }

                completeOrdersList.reverse()    // latest first

                setRecycler()
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun setRecycler() {

        val names = mutableListOf<String>()
        val paymentStatus = mutableListOf<Boolean>()

        for (order in completeOrdersList) {
            names.add(order.userName ?: "Unknown")
            paymentStatus.add(order.paymentReceived)
        }

        val adapter = deleveryadapter(names, paymentStatus)

        binding.deleveryrecyclerview.layoutManager =
            LinearLayoutManager(this@outfordeleveryActivity)

        binding.deleveryrecyclerview.adapter = adapter
    }
}
