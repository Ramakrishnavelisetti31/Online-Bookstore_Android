package com.example.bookstore.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bookstore.model.AuthListener
import com.example.bookstore.model.Customer
import com.example.bookstore.service.UserAuthService

class LoginViewModel: ViewModel() {
    private var userAuthService = UserAuthService()

    private val _loginStatus = MutableLiveData<AuthListener>()
    val loginStatus =  _loginStatus as LiveData<AuthListener>

    fun logIn(customer: Customer) {
        userAuthService.userLogin(customer) {
            if (it.status) {
                _loginStatus.value = it
            }
        }
    }
}

