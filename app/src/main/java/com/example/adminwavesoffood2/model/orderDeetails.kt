package com.example.adminwavesoffood2.model

import java.io.Serializable

data class orderDetails(

    var userUid: String? = null,
    var userName: String? = null,
    var phoneNumber: String? = null,
    var address: String? = null,

    var foodNames: MutableList<String> = mutableListOf(),
    var foodPrices: MutableList<String> = mutableListOf(),
    var foodImages: MutableList<String> = mutableListOf(),

    var foodQuantities: MutableList<Any> = mutableListOf(),
    var totalPrice: Any? = null,

    var itemPushKey: String? = null,
    var paymentReceived: Boolean = false,
    var orderAccepted: Boolean = false,
    var currentTime: Any? = null

) : Serializable
