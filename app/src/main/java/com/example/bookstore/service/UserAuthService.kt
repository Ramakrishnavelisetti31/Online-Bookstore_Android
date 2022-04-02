package com.example.bookstore.service

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.util.Log
import android.widget.Toast
import com.example.bookstore.adapter.CartAdapter
import com.example.bookstore.api.ApiClass
import com.example.bookstore.database.CustomerDatabase
import com.example.bookstore.model.*
import com.example.bookstore.viewmodel.NetworkConnectivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserAuthService {

    private var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private var firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private var apiClass = ApiClass()
    private lateinit var customerDatabase: CustomerDatabase

    fun userLogin(customer: Customer, listener: (AuthListener) -> Unit) {
        firebaseAuth.signInWithEmailAndPassword(customer.email,customer.password).addOnCompleteListener {
            if (it.isSuccessful) {
                listener(AuthListener(true, "Sign in Successfully"))
            }
        }
    }

    fun sqlLogin(customer: Customer, context: Context, listener: (AuthListener) -> Unit) {
        customerDatabase = CustomerDatabase(context)
        val isPresent = customerDatabase.loginCheck(customer.email, customer.password)
        if (isPresent) {
            listener(AuthListener(true, "Sign in Successfully"))

        }
    }

    fun registerUser(customer: Customer, context: Context, listener: (AuthListener) -> Unit) {
        firebaseAuth.createUserWithEmailAndPassword(customer.email, customer.password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    storeDataFirestore(customer, context, listener)
                    listener(AuthListener(true, "Register Successfully"))
                }
            }
    }

    fun signUpWithApi(email: String, password: String, listener: (AuthListener) -> Unit) {
        apiClass.getService().register(Customer(email = email, password = password, returnSecureToken = true))
            .enqueue(object : Callback<RegisterResponse> {
                override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                    if (response.isSuccessful) {
                        listener(AuthListener(true, "Logged in Successfully"))
                    }
                }
                override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                    val message = t.localizedMessage
                    Log.d("ramakrishna", "$message")
                }
            })
    }

    fun logInWithApi(email: String, password: String, listener: (AuthListener) -> Unit) {
        apiClass.getService().login(Customer(email = email, password = password, returnSecureToken = true))
            .enqueue(object : Callback<LoginResponse> {
                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    if (response.isSuccessful) {
                        listener(AuthListener(true, "Logged in Successfully"))
                    }
                }
                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    val message = t.localizedMessage
                    Log.d("ramakrishna", "$message")
                }
            })
    }

    private fun storeDataFirestore(customer: Customer, context: Context, listener: (AuthListener) -> Unit) {
        firebaseAuth.currentUser?.let {
            customer.customer_id = it.uid
            val customerDB: MutableMap<String, Any> = HashMap()
            customerDB["userName"] = customer.userName
            customerDB["email"] = customer.email
            customerDB["password"] = customer.password
            customerDB["customer_id"] = customer.customer_id
            customerDatabase = CustomerDatabase(context)
            customerDatabase.saveData(customer)
            firestore.collection("users").document(it.uid)
                .set(customerDB)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        listener(AuthListener(true, "Added data successfully"))
                    } else {
                        Log.d(ContentValues.TAG, "get failed with ", it.exception)
                    }
                }
        }
    }

    fun sendAddressDetails(customer: Customer) {
        val customerDB: MutableMap<String, Any> = HashMap()
        customerDB["fullName"] = customer.fullName
        customerDB["phoneNumber"] = customer.mobileNumber
        customerDB["address"] = customer.address
        customerDB["city"] = customer.city
        customerDB["state"] = customer.state
        customerDB["pincode"] = customer.pinCode
        firebaseAuth.currentUser?.let {
            firestore.collection("users").document(it.uid)
                .update(customerDB).addOnCompleteListener {
                    if (it.isSuccessful) {
                        Log.d("UserAuthService", "address details added")
                    } else {
                        Log.d(ContentValues.TAG, "get failed with ", it.exception)
                    }
                }
        }
    }

    fun getDataFromFirestore(context: Context, listener: (AuthListener) -> Unit) {
        firestore.collection("users").document(firebaseAuth.currentUser!!.uid).get()
            .addOnCompleteListener {
                customerDatabase = CustomerDatabase(context)
                customerDatabase.getData()
                if (it.result.exists()) {
                    val userName = it.result.getString("userName").toString()
                    val email = it.result.getString("email").toString()
                    val fullName = it.result.getString("fullName").toString()
                    val phone = it.result.getString("phoneNumber").toString()
                    val address = it.result.getString("address").toString()
                    val city = it.result.getString("city").toString()
                    val state = it.result.getString("state").toString()
                    val pinCode = it.result.getString("pincode").toString()
                    val id = firebaseAuth.currentUser!!.uid
                    SharedPreference.initSharedPreferences(context)
                    if (address.isNullOrEmpty()) {
                        SharedPreference.addString(Constant.CUSTOMER_USER_NAME, userName)
                        SharedPreference.addString(Constant.CUSTOMER_EMAIL, email)
                        SharedPreference.addString(Constant.CUSTOMER_ID, id)
                    } else {
                        SharedPreference.addString(Constant.CUSTOMER_FULL_NAME, fullName)
                        SharedPreference.addString(Constant.CUSTOMER_PHONE, phone)
                        SharedPreference.addString(Constant.CUSTOMER_ADDRESS, address)
                        SharedPreference.addString(Constant.CUSTOMER_CITY, city)
                        SharedPreference.addString(Constant.CUSTOMER_STATE, state)
                        SharedPreference.addString(Constant.CUSTOMER_PINCODE, pinCode)
                    }
                    listener(AuthListener(true, "Fetched data successfully"))
                } else {
                    Log.d("UserAuthService", "get the data $it")
                }
            }
    }

    fun logOut(listener: (AuthListener) -> Unit) {
        firebaseAuth.signOut()
        listener(AuthListener(true, "sign out successfully"))
    }

}