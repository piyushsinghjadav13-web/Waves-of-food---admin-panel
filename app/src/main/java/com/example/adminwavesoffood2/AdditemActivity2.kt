package com.example.adminwavesoffood2

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.adminwavesoffood2.databinding.ActivityAdditem2Binding
import com.example.adminwavesoffood2.model.AllMenu
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AdditemActivity2 : AppCompatActivity() {

    // Food item Details
    private var foodName: String = ""
    private var foodPrice: String = ""
    private var foodDescription: String = ""
    private var foodIngredient: String = ""
    private var foodImageUri: Uri? = null

    // Firebase
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    private val binding: ActivityAdditem2Binding by lazy {
        ActivityAdditem2Binding.inflate(layoutInflater)
    }

    // Image Picker (modern way)
    private val pickImage = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            foodImageUri = uri
            binding.addimage.setImageURI(uri)
        } else {
            Toast.makeText(this, "No Image Selected", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // Init Firebase
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        // Select image button
        binding.selectimage.setOnClickListener {
            pickImage.launch(
                PickVisualMediaRequest(
                    ActivityResultContracts.PickVisualMedia.ImageOnly
                )
            )
        }

        // Add Item button
        binding.additembtn.setOnClickListener {
            getFoodDetails()
        }

        // Back Btn
        binding.backbtn.setOnClickListener {
            finish()
        }
    }

    private fun getFoodDetails() {
        foodName = binding.enterfoodname.text.toString().trim()
        foodPrice = binding.enterfoodprice.text.toString().trim()
        foodDescription = binding.shortdiscription.text.toString().trim()
        foodIngredient = binding.ingredients.text.toString().trim()

        if (foodName.isEmpty() ||
            foodPrice.isEmpty() ||
            foodDescription.isEmpty() ||
            foodIngredient.isEmpty()
        ) {
            Toast.makeText(this, "Fill all the details", Toast.LENGTH_SHORT).show()
            return
        }

        if (foodImageUri == null) {
            Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show()
            return
        }

        // call upload
        uploadData()
    }

    private fun uploadData() {
        val menuRef: DatabaseReference = database.getReference("menu")
        val newItemKey: String? = menuRef.push().key

        if (newItemKey == null) {
            Toast.makeText(this, "Error generating item ID", Toast.LENGTH_SHORT).show()
            return
        }

        /**
         *  🔥 IMPORTANT:
         *  Yaha image upload hoga (Cloudinary ya Firebase Storage)
         *  Tumne अभी upload service final nahi ki — isliye main
         *  temporary local URL store kar raha hoon.
         *
         *  Later:
         *  uploadImage(foodImageUri!!) → get downloadUrl → Save in DB
         */

        val newItem = AllMenu(
            foodName = foodName,
            foodPrice = foodPrice,
            foodDescription = foodDescription,
            foodIngredient = foodIngredient,
            foodImage = foodImageUri.toString()   // temporary path
        )

        menuRef.child(newItemKey).setValue(newItem)
            .addOnSuccessListener {
                Toast.makeText(this, "Item added successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to add item", Toast.LENGTH_SHORT).show()
            }
    }
}
