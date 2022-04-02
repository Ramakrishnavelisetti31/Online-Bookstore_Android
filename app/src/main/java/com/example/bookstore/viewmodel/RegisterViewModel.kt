package com.example.bookstore.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bookstore.model.AuthListener
import com.example.bookstore.model.Customer
import com.example.bookstore.service.UserAuthService

class RegisterViewModel: ViewModel() {
    private var userAuthService = UserAuthService()

    private val _registrationStatus = MutableLiveData<AuthListener>()
    val registrationStatus =  _registrationStatus as LiveData<AuthListener>

    fun register(customer: Customer, context: Context) {
        userAuthService.registerUser(customer, context) {
            if (it.status) {
                _registrationStatus.value = it
            }
        }
    }

    fun apiRegister(customer: Customer) {
        userAuthService.signUpWithApi(customer.email, customer.password) {
            if (it.status) {
                _registrationStatus.value = it
            }
        }
    }
}