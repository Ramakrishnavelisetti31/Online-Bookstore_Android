package com.example.bookstore.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bookstore.model.AuthListener
import com.example.bookstore.model.Book
import com.example.bookstore.service.BookService

class WishListViewModel: ViewModel() {
    private val bookService = BookService()

    private val _getWishlistStatus = MutableLiveData<AuthListener>()
    val getWishlistStatus =  _getWishlistStatus as LiveData<AuthListener>

    fun getWishlist(wishlist: ArrayList<Book>, context: Context) {
        bookService.getWishlistItem(wishlist, context) {
            if (it.status) {
                _getWishlistStatus.value = it
            }
        }
    }
}