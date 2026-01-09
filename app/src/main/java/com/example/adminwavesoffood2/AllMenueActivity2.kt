package com.example.adminwavesoffood2

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adminwavesoffood2.adapter.AllItemAdapter
import com.example.adminwavesoffood2.databinding.ActivityAllMenue2Binding
import com.example.adminwavesoffood2.model.AllMenu
import com.google.firebase.database.*

class AllMenueActivity2 : AppCompatActivity() {

    private lateinit var database: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    private var menuItems: ArrayList<AllMenu> = ArrayList()

    private val binding: ActivityAllMenue2Binding by lazy {
        ActivityAllMenue2Binding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        database = FirebaseDatabase.getInstance()
        databaseReference = database.reference

        retrieveMenuItems()

        binding.backbtn.setOnClickListener { finish() }
    }

    private fun retrieveMenuItems() {

        val foodRef: DatabaseReference = database.reference.child("menu")

        foodRef.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                menuItems.clear()

                for (foodSnapshot in snapshot.children) {

                    val menuItem = foodSnapshot.getValue(AllMenu::class.java)

                    if (menuItem != null) {

                        // ⭐ IMPORTANT: Firebase ka real key store kar rahe hain
                        menuItem.menuItemId = foodSnapshot.key

                        menuItems.add(menuItem)
                    }
                }

                setAdapter()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("DatabaseError", "Error: ${error.message}")
            }
        })
    }

    private fun setAdapter() {
        val adapter = AllItemAdapter(this, menuItems)
        binding.menurecycler.layoutManager = LinearLayoutManager(this)
        binding.menurecycler.adapter = adapter
    }
}
