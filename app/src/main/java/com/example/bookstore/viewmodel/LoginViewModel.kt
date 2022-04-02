package com.example.bookstore.viewmodel

import android.content.Context
import android.database.Cursor
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

    fun apiLogIn(customer: Customer) {
        userAuthService.logInWithApi(customer.email, customer.password) {
            if (it.status) {
                _loginStatus.value = it
            }
        }
    }

    fun localLogIn(customer: Customer, context: Context) =
        userAuthService.sqlLogin(customer, context) {
            if (it.status) {
                _loginStatus.value = it
            }
        }
}

