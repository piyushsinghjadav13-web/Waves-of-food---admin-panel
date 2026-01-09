package com.example.adminwavesoffood2.model

data class AllMenu(
    var menuItemId: String? = "",     // ⭐ Firebase push key (IMPORTANT)

    var foodName : String? = "",
    var foodPrice : String? = "",
    var foodImage : String? = "",
    var foodDescription : String? = "",
    var foodIngredient : String? = "",
    var foodCategory : String? = "",
    var foodQuantity : String? = ""
)
