package com.example.adminwavesoffood2.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.adminwavesoffood2.databinding.OrderDetaillsItemsBinding

class OrderDetailsAdapter(
    private val context: Context,
    private val foodNames: ArrayList<String>,
    private val foodImages: ArrayList<String>,
    private val foodQuantities: ArrayList<String>,
    private val foodPrices: ArrayList<String>
) : RecyclerView.Adapter<OrderDetailsAdapter.OrderDetailsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderDetailsViewHolder {
        val binding = OrderDetaillsItemsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return OrderDetailsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderDetailsViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = foodNames.size

    inner class OrderDetailsViewHolder(private val b: OrderDetaillsItemsBinding) :
        RecyclerView.ViewHolder(b.root) {

        fun bind(pos: Int) {

            b.foodname.text = foodNames[pos]
            b.quantity.text = foodQuantities[pos]
            b.foodprices.text = "₹ ${foodPrices[pos]}"

            // ⭐ SAFE IMAGE LOAD
            val img = foodImages.getOrNull(pos)

            if (img.isNullOrEmpty()) {
                b.foodimage.setImageResource(com.example.adminwavesoffood2.R.drawable.wafels2)
            } else {
                Glide.with(context)
                    .load(Uri.parse(img))
                    .into(b.foodimage)
            }
        }
    }
}
