package com.example.adminwavesoffood2

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.adminwavesoffood2.databinding.ActivityAdminProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class AdminProfile : AppCompatActivity() {

    private val binding: ActivityAdminProfileBinding by lazy {
        ActivityAdminProfileBinding.inflate(layoutInflater)
    }

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var addminReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        addminReference = database.reference.child("user")

        binding.backbtn.setOnClickListener { finish() }
        binding.saveinfo.setOnClickListener { updateUserData() }

        disableFields()

        var isEnable = false
        binding.editbtn.setOnClickListener {
            isEnable = !isEnable
            enableFields(isEnable)
        }

        retrieveUserData()
    }

    private fun disableFields() {
        binding.name.isEnabled = false
        binding.phone.isEnabled = false
        binding.address.isEnabled = false
        binding.email.isEnabled = false
        binding.password.isEnabled = false
        binding.saveinfo.isEnabled = false
    }

    private fun enableFields(enable: Boolean) {
        binding.name.isEnabled = enable
        binding.phone.isEnabled = enable
        binding.address.isEnabled = enable
        binding.email.isEnabled = enable
        binding.password.isEnabled = enable
        binding.saveinfo.isEnabled = enable
    }

    private fun retrieveUserData() {
        val currentUserUid = auth.currentUser?.uid
        if (currentUserUid != null) {
            val userReference = addminReference.child(currentUserUid)

            userReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    val name = snapshot.child("name").value
                    val email = snapshot.child("email").value
                    val password = snapshot.child("password").value
                    val phone = snapshot.child("phone").value
                    val address = snapshot.child("address").value

                    setDataToTextView(name, email, password, phone, address)
                }

                override fun onCancelled(error: DatabaseError) {}
            })
        }
    }

    private fun setDataToTextView(
        ownerName: Any?,
        ownerEmail: Any?,
        ownerPassword: Any?,
        ownerPhone: Any?,
        ownerAddress: Any?
    ) {
        binding.name.setText(ownerName.toString())
        binding.email.setText(ownerEmail.toString())
        binding.password.setText(ownerPassword.toString())
        binding.phone.setText(ownerPhone.toString())
        binding.address.setText(ownerAddress.toString())
    }

    private fun updateUserData() {

        val updateName = binding.name.text.toString()
        val updatePhone = binding.phone.text.toString()
        val updateEmail = binding.email.text.toString()
        val updateAddress = binding.address.text.toString()
        val updatePassword = binding.password.text.toString()

        val currentUserUid = auth.currentUser?.uid

        if (currentUserUid != null) {

            val userReference = addminReference.child(currentUserUid)

            userReference.child("name").setValue(updateName)
            userReference.child("email").setValue(updateEmail)
            userReference.child("password").setValue(updatePassword)
            userReference.child("phone").setValue(updatePhone)
            userReference.child("address").setValue(updateAddress)

            // Firebase Auth update
            auth.currentUser?.updateEmail(updateEmail)
            auth.currentUser?.updatePassword(updatePassword)

            Toast.makeText(this, "Profile Updated Successfully", Toast.LENGTH_SHORT).show()

        } else {
            Toast.makeText(this, "Profile Update Failed", Toast.LENGTH_SHORT).show()
        }
    }
}
