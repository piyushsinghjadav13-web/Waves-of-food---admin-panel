package com.example.adminwavesoffood2

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adminwavesoffood2.adapter.OrderDetailsAdapter
import com.example.adminwavesoffood2.databinding.ActivityAllorderBinding
import com.example.adminwavesoffood2.model.orderDetails

class AllorderActivity : AppCompatActivity() {

    private val binding: ActivityAllorderBinding by lazy {
        ActivityAllorderBinding.inflate(layoutInflater)
    }

    private var foodName = ArrayList<String>()
    private var foodImage = ArrayList<String>()
    private var foodQuantity = ArrayList<String>()
    private var foodPrice = ArrayList<String>()

    private var userName: String? = null
    private var address: String? = null
    private var phoneNumber: String? = null
    private var totalPrice: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        binding.backbtn1.setOnClickListener { finish() }

        getDataFromIntent()
    }

    private fun getDataFromIntent() {
        val order = intent.getSerializableExtra("UserorderDetails") as? orderDetails

        order?.let {

            userName = it.userName
            address = it.address
            phoneNumber = it.phoneNumber
            totalPrice = it.totalPrice?.toString()

            foodName = ArrayList(it.foodNames)
            foodImage = ArrayList(it.foodImages)
            foodQuantity = it.foodQuantities.map { q -> q.toString() }.toCollection(ArrayList())
            foodPrice = it.foodPrices.map { p -> p.toString() }.toCollection(ArrayList())

            setUserDetails()
            setAdapter()
        }
    }

    private fun setAdapter() {
        binding.recyclerorder.layoutManager = LinearLayoutManager(this)
        binding.recyclerorder.adapter = OrderDetailsAdapter(
            this,
            foodName,
            foodImage,
            foodQuantity,
            foodPrice
        )
    }

    private fun setUserDetails() {
        binding.name1.text = userName
        binding.Address.text = address
        binding.Phone.text = phoneNumber
        binding.totalpay.text = totalPrice    // ⭐ PRICE NOW SHOWING
    }
}
