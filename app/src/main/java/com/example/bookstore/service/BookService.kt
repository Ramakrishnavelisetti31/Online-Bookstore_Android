package com.example.bookstore.service

import android.content.Context
import android.util.Log
import androidx.core.net.toUri
import com.example.bookstore.model.AuthListener
import com.example.bookstore.model.Book
import com.google.firebase.firestore.FirebaseFirestore

class BookService {
    private var firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    fun getBookDataFromFirestore(bookList: ArrayList<Book>, listener: (AuthListener) -> Unit) {
        firestore.collection("books").get()
            .addOnSuccessListener { list ->
                for (document in list) {
                    val name = document.getString("book_name").toString()
                    val authorName = document.getString("book_author").toString()
                    val price = document.getString("book_price").toString()
                    val img_url = document.getString("book_imgurl")?.toUri().toString()
                    val book = Book(name, authorName, price, img_url)
                    bookList.add(book)
                    Log.d("Noteservice", "${bookList.size}")
                    listener(AuthListener(true, "Fetch notes successfully"))
                }
            }
    }

}