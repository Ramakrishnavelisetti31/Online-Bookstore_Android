package com.example.bookstore.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bookstore.model.AuthListener
import com.example.bookstore.service.UserAuthService

class ProfileViewModel: ViewModel() {
    private var userAuthService = UserAuthService()

    private val _profileStatus = MutableLiveData<AuthListener>()
    val profileStatus =  _profileStatus as LiveData<AuthListener>

    fun getData(context: Context) {
        userAuthService.getDataFromFirestore(context) {
            _profileStatus.value = it
        }
    }

}