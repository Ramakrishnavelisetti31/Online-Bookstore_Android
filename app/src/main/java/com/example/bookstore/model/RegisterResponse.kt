package com.example.bookstore.model

data class RegisterResponse(
    var idToken: String,
    var email: String,
    var refreshToken: String,
    var expiresIn: String,
    var localId: String,
)
