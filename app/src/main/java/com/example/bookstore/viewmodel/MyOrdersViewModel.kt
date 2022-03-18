package com.example.bookstore.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bookstore.model.AuthListener
import com.example.bookstore.model.Book
import com.example.bookstore.service.BookService

class MyOrdersViewModel: ViewModel() {
    private val bookService = BookService()

    private val _getOrderStatus = MutableLiveData<AuthListener>()
    val getOrderStatus =  _getOrderStatus as LiveData<AuthListener>

    fun getOrders(orderList: ArrayList<Book>) {
        bookService.getMyOrders(orderList) {
            if (it.status) {
                _getOrderStatus.value = it
            }
        }
    }

}