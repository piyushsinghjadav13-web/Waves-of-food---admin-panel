package com.example.adminwavesoffood2.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.adminwavesoffood2.AllMenueActivity2
import com.example.adminwavesoffood2.databinding.ItemItemBinding
import com.example.adminwavesoffood2.model.AllMenu
import com.google.firebase.database.FirebaseDatabase

class AllItemAdapter(
    private val context: AllMenueActivity2,
    private val menuList: ArrayList<AllMenu>
) : RecyclerView.Adapter<AllItemAdapter.AddItemViewHolder>() {

    private val itemquantities = IntArray(menuList.size) { 1 }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddItemViewHolder {
        val binding = ItemItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AddItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AddItemViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = menuList.size

    inner class AddItemViewHolder(private val binding: ItemItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int) {

            val menuItem = menuList[position]

            val uri = Uri.parse(menuItem.foodImage)
            Glide.with(context).load(uri).into(binding.foodimage)

            binding.foodname.text = menuItem.foodName
            binding.foodprice.text = menuItem.foodPrice
            binding.quantitytext.text = itemquantities[position].toString()

            // ⭐ DELETE BUTTON
            binding.deletebtn.setOnClickListener {

                val itemKey = menuItem.menuItemId

                if (itemKey != null) {

                    FirebaseDatabase.getInstance().reference
                        .child("menu")
                        .child(itemKey)
                        .removeValue()
                        .addOnSuccessListener {

                            menuList.removeAt(position)
                            notifyItemRemoved(position)
                            notifyItemRangeChanged(position, menuList.size)

                            Toast.makeText(context, "Item Deleted", Toast.LENGTH_SHORT).show()
                        }
                }
            }
        }
    }
}
