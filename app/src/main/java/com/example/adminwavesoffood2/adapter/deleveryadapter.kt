package com.example.adminwavesoffood2.adapter

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.adminwavesoffood2.databinding.DeleveryitemBinding

class deleveryadapter(
    private val customerName: MutableList<String>,
    private val paymentStatus: MutableList<Boolean>
) : RecyclerView.Adapter<deleveryadapter.deleveryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): deleveryViewHolder {
        val binding = DeleveryitemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return deleveryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: deleveryViewHolder, position: Int) {
        holder.bind(
            customerName[position],
            paymentStatus[position]
        )
    }

    override fun getItemCount(): Int = customerName.size

    class deleveryViewHolder(private val binding: DeleveryitemBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(name: String, status: Boolean) {

            val statusText = if (status) "Received" else "Not Received"
            val statusColor = if (status) Color.GREEN else Color.RED

            binding.customername.text = name
            binding.moneystatus.text = statusText

            binding.moneystatus.setTextColor(statusColor)
            binding.statusDot.backgroundTintList = ColorStateList.valueOf(statusColor)
        }
    }
}
