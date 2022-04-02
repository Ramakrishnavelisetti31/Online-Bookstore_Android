package com.example.bookstore.api

import com.example.bookstore.model.Customer
import com.example.bookstore.model.LoginResponse
import com.example.bookstore.model.RegisterResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiInterface {

    @POST("./accounts:signUp?key=AIzaSyBPHc3kkODhWi6rthMmUy1aBxfWtpR2LzY")
    fun register(@Body request: Customer): Call<RegisterResponse>

    @POST("./accounts:signInWithPassword?key=AIzaSyBPHc3kkODhWi6rthMmUy1aBxfWtpR2LzY")
    fun login(@Body request: Customer): Call<LoginResponse>
}