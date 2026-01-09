package com.example.adminwavesoffood2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adminwavesoffood2.adapter.Pendingorderadapter
import com.example.adminwavesoffood2.databinding.ActivityPendingorderBinding
import com.example.adminwavesoffood2.model.orderDetails
import com.google.firebase.database.*

class Pendingorder : AppCompatActivity() {

    private lateinit var binding: ActivityPendingorderBinding
    private val database = FirebaseDatabase.getInstance()

    private val nameList = ArrayList<String>()
    private val qtyList = ArrayList<String>()
    private val imageList = ArrayList<String>()
    private val orderIdList = ArrayList<String>()
    private val acceptedList = ArrayList<Boolean>()
    private val fullOrderList = ArrayList<orderDetails>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPendingorderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backbtn.setOnClickListener { finish() }
        loadOrders()
    }

    private fun loadOrders() {

        database.reference.child("orderDetails")
            .addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {

                    nameList.clear()
                    qtyList.clear()
                    imageList.clear()
                    orderIdList.clear()
                    acceptedList.clear()
                    fullOrderList.clear()

                    for (order in snapshot.children) {

                        val fullOrder = orderDetails(
                            userUid = order.child("userUid").value?.toString(),
                            userName = order.child("userName").value?.toString(),
                            phoneNumber = order.child("phoneNumber").value?.toString(),
                            address = order.child("address").value?.toString(),

                            foodNames = order.child("foodNames").children.map { it.value.toString() }.toMutableList(),
                            foodPrices = order.child("foodPrices").children.map { it.value.toString() }.toMutableList(),
                            foodImages = order.child("foodImages").children.map { it.value.toString() }.toMutableList(),
                            foodQuantities = order.child("foodQuantities").children.map { it.value!! }.toMutableList(),

                            totalPrice = order.child("totalPrice").value,
                            itemPushKey = order.child("itemPushKey").value?.toString(),
                            paymentReceived = order.child("paymentReceived").value == true,
                            orderAccepted = order.child("orderAccepted").value == true,
                            currentTime = order.child("currentTime").value
                        )

                        // DEBUG
                        println("DEBUG → Loaded order: userUid=${fullOrder.userUid}, itemKey=${fullOrder.itemPushKey}")

                        fullOrderList.add(fullOrder)

                        nameList.add(fullOrder.userName ?: "Unknown")
                        qtyList.add("Items: ${fullOrder.foodNames.size}")
                        imageList.add(fullOrder.foodImages.firstOrNull() ?: "")
                        orderIdList.add(order.key ?: "")
                        acceptedList.add(fullOrder.orderAccepted)
                    }

                    setAdapter()
                }

                override fun onCancelled(error: DatabaseError) {}
            })
    }

    private fun setAdapter() {

        val adapter = Pendingorderadapter(
            this,
            nameList,
            qtyList,
            imageList,
            orderIdList,
            acceptedList,
            fullOrderList,
            object : Pendingorderadapter.onItemClicked {

                override fun onItemClickListener(position: Int) {
                    val intent = Intent(this@Pendingorder, AllorderActivity::class.java)
                    intent.putExtra("UserorderDetails", fullOrderList[position])
                    startActivity(intent)
                }

                override fun onItemAcceptClickListener(position: Int) {}
                override fun onItemDispatchClickListener(position: Int) {}
            }
        )

        binding.pendingrecycler.layoutManager = LinearLayoutManager(this)
        binding.pendingrecycler.adapter = adapter
    }
}
