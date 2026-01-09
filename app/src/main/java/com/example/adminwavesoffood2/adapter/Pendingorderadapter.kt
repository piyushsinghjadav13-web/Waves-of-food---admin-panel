package com.example.adminwavesoffood2.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.adminwavesoffood2.R
import com.example.adminwavesoffood2.databinding.PendingorderitemsBinding
import com.example.adminwavesoffood2.model.orderDetails
import com.google.firebase.database.FirebaseDatabase

class Pendingorderadapter(
    private val context: Context,
    private val nameList: ArrayList<String>,
    private val qtyList: ArrayList<String>,
    private val foodImages: ArrayList<String>,
    private val orderIdList: ArrayList<String>,
    private val acceptedList: ArrayList<Boolean>,
    private val fullOrderList: ArrayList<orderDetails>,
    private val itemClicked: onItemClicked
) : RecyclerView.Adapter<Pendingorderadapter.Holder>() {

    interface onItemClicked {
        fun onItemClickListener(position: Int)
        fun onItemAcceptClickListener(position: Int)
        fun onItemDispatchClickListener(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        Holder(
            PendingorderitemsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun getItemCount() = nameList.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(position)
    }

    inner class Holder(private val b: PendingorderitemsBinding) :
        RecyclerView.ViewHolder(b.root) {

        fun bind(pos: Int) {

            b.foodname.text = nameList[pos]
            b.quantity.text = qtyList[pos]

            if (foodImages[pos].isNotEmpty()) {
                Glide.with(context).load(Uri.parse(foodImages[pos])).into(b.foodimage)
            } else {
                b.foodimage.setImageResource(R.drawable.wafels2)
            }

            itemView.setOnClickListener {
                if (!b.acceptbtn.isPressed) {
                    itemClicked.onItemClickListener(pos)
                }
            }

            b.acceptbtn.text = if (acceptedList[pos]) "Dispatch" else "Accept"

            b.acceptbtn.setOnClickListener {
                b.acceptbtn.isPressed = true

                val databaseRef = FirebaseDatabase.getInstance().reference
                val orderId = orderIdList[pos]
                val fullOrder = fullOrderList[pos]

                val userUid = fullOrder.userUid
                val itemKey = fullOrder.itemPushKey

                // Null Check
                if (userUid.isNullOrEmpty() || itemKey.isNullOrEmpty()) {
                    toast("Error: Missing userUid or itemKey")
                    return@setOnClickListener
                }

                // ---------------------------------------------------
                // 1️⃣ ACCEPT ORDER (FIRST CLICK)
                // ---------------------------------------------------
                if (!acceptedList[pos]) {

                    // Update orderDetails → orderAccepted = true
                    databaseRef.child("orderDetails")
                        .child(orderId)
                        .child("orderAccepted")
                        .setValue(true)
                        .addOnSuccessListener {

                            // Update user → BuyHistory → orderAccepted = true
                            databaseRef.child("user")
                                .child(userUid)
                                .child("BuyHistory")
                                .child(itemKey)
                                .child("orderAccepted")
                                .setValue(true)
                                .addOnSuccessListener {

                                    acceptedList[pos] = true
                                    b.acceptbtn.text = "Dispatch"
                                    toast("Order Accepted ✓ and User Updated ✓")
                                    itemClicked.onItemAcceptClickListener(pos)
                                }
                                .addOnFailureListener {
                                    toast("Failed to update user's BuyHistory")
                                }
                        }
                        .addOnFailureListener {
                            toast("Failed to update orderDetails")
                        }
                }

                // ---------------------------------------------------
                // 2️⃣ DISPATCH ORDER (SECOND CLICK)
                // ---------------------------------------------------
                else {

                    databaseRef.child("dispatchOrders")
                        .child(orderId)
                        .setValue(fullOrder)
                        .addOnSuccessListener {

                            // Remove from Pending Orders
                            databaseRef.child("orderDetails")
                                .child(orderId)
                                .removeValue()

                            toast("Order Dispatched ✓")
                            removeItem(pos)
                            itemClicked.onItemDispatchClickListener(pos)
                        }
                        .addOnFailureListener {
                            toast("Failed to dispatch order")
                        }
                }
            }
        }

        private fun removeItem(pos: Int) {
            nameList.removeAt(pos)
            qtyList.removeAt(pos)
            foodImages.removeAt(pos)
            orderIdList.removeAt(pos)
            acceptedList.removeAt(pos)
            fullOrderList.removeAt(pos)
            notifyItemRemoved(pos)
        }

        private fun toast(msg: String) =
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }
}
