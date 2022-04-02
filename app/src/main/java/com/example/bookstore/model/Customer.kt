package com.example.bookstore.model

data class Customer(
    var userName: String = "",
    var email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    var fullName: String = "",
    var mobileNumber: String = "",
    var address: String = "",
    var city: String = "",
    var state: String = "",
    var pinCode: String = "",
    var customer_id: String = "",
    var returnSecureToken: Boolean = true
)

