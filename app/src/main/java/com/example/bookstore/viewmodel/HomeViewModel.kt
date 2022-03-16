package com.example.bookstore.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bookstore.model.AuthListener
import com.example.bookstore.model.Book
import com.example.bookstore.service.BookService
import com.example.bookstore.service.UserAuthService

class HomeViewModel: ViewModel() {
    private var userAuthService = UserAuthService()
    private var bookService = BookService()

    private val _logoutStatus = MutableLiveData<AuthListener>()
    val logoutStatus =  _logoutStatus as LiveData<AuthListener>

    private val _getBookStatus = MutableLiveData<AuthListener>()
    val getBookStatus =  _getBookStatus as LiveData<AuthListener>

    fun signOut() {
        userAuthService.logOut() {
            _logoutStatus.value = it
        }
    }

    fun getBooks(bookList: ArrayList<Book>) {
        bookService.getBookDataFromFirestore(bookList) {
            _getBookStatus.value = it
        }
    }

}