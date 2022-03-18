package com.example.bookstore.model

import java.io.Serializable

data class Book(
    var book_Name: String = "",
    var book_Author: String = "",
    var book_Price: String = "",
    var book_imgUrl: String = "",
    var book_id: String = "",
    var user_id: String = "",
    var expand: Boolean = false
) : Serializable
