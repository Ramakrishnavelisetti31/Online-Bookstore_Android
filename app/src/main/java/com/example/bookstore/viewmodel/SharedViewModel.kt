package com.example.bookstore.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel: ViewModel() {
    private val _goToLoginPageStatus = MutableLiveData<Boolean>()
    var goToLoginPageStatus = _goToLoginPageStatus as LiveData<Boolean>

    private val _goToRegistrationPageStatus = MutableLiveData<Boolean>()
    val goToRegistrationPageStatus = _goToRegistrationPageStatus as LiveData<Boolean>

    private val _goToHomePageStatus = MutableLiveData<Boolean>()
    val goToHomePageStatus = _goToHomePageStatus as LiveData<Boolean>

    private val _goToProfilePageStatus = MutableLiveData<Boolean>()
    val goToProfilePageStatus = _goToProfilePageStatus as LiveData<Boolean>

    private val _goToCartPageStatus = MutableLiveData<Boolean>()
    val goToCartPageStatus = _goToCartPageStatus as LiveData<Boolean>

    private val _goToWishListPageStatus = MutableLiveData<Boolean>()
    val goToWishListPageStatus = _goToWishListPageStatus as LiveData<Boolean>

    private val _goToMyOrderPageStatus = MutableLiveData<Boolean>()
    val goToMyOrderPageStatus = _goToMyOrderPageStatus as LiveData<Boolean>

    private val _goToFinalPageStatus = MutableLiveData<Boolean>()
    val goToFinalPageStatus = _goToFinalPageStatus as LiveData<Boolean>

    fun setGoToLoginPageStatus(status: Boolean) {
        _goToLoginPageStatus.value = status
    }

    fun setGoToRegistrationPageStatus(status: Boolean) {
        _goToRegistrationPageStatus.value = status
    }

    fun setGoToHomePageStatus(status: Boolean) {
        _goToHomePageStatus.value = status
    }

    fun setGoToProfilePageStatus(status: Boolean) {
        _goToProfilePageStatus.value = status
    }

    fun setGoToCartPageStatus(status: Boolean) {
        _goToCartPageStatus.value = status
    }

    fun setGoToWishListPageStatus(status: Boolean) {
        _goToWishListPageStatus.value = status
    }

    fun setGoToMyOrderPageStatus(status: Boolean) {
        _goToMyOrderPageStatus.value = status
    }

    fun setGoToFinalPageStatus(status: Boolean) {
        _goToFinalPageStatus.value = status
    }


}