package com.example.bookstore.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bookstore.model.AuthListener
import com.example.bookstore.model.Book
import com.example.bookstore.service.BookService

class CartViewModel: ViewModel() {
    private val bookService = BookService()

    private val _getCartStatus = MutableLiveData<AuthListener>()
    val getCartStatus =  _getCartStatus as LiveData<AuthListener>

    fun getCart(bookList: ArrayList<Book>) {
        bookService.getCartItem(bookList) {
            if (it.status) {
                _getCartStatus.value = it
            }
        }
    }

}
