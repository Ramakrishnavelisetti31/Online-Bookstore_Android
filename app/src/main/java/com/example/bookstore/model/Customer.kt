package com.example.bookstore.model

data class Customer(
    var name: String = "",
    var email: String = "",
    var mobileNumber: String = "",
    val password: String = "",
    val confirmPassword: String = "",
 )

