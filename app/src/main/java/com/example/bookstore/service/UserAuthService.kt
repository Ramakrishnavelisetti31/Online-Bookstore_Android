package com.example.bookstore.service

import android.content.ContentValues
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.bookstore.adapter.CartAdapter
import com.example.bookstore.model.AuthListener
import com.example.bookstore.model.Constant
import com.example.bookstore.model.Customer
import com.example.bookstore.model.SharedPreference
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class UserAuthService {

    private var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private var firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    fun userLogin(customer: Customer, listener: (AuthListener) -> Unit) {
        firebaseAuth.signInWithEmailAndPassword(customer.email,customer.password).addOnCompleteListener {
            if (it.isSuccessful) {
                listener(AuthListener(true, "Sign in Successfully"))
            }
        }
    }

    fun registerUser(customer: Customer, listener: (AuthListener) -> Unit) {
        firebaseAuth.createUserWithEmailAndPassword(customer.email, customer.password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    storeDataFirestore(customer,listener)
                    listener(AuthListener(true, "Register Successfully"))
                }
            }
    }

    private fun storeDataFirestore(customer: Customer, listener: (AuthListener) -> Unit) {
        firebaseAuth.currentUser?.let {
            customer.customer_id = it.uid
            val customerDB: MutableMap<String, Any> = HashMap()
            customerDB["userName"] = customer.userName
            customerDB["email"] = customer.email
            customerDB["password"] = customer.password
            customerDB["customer_id"] = customer.customer_id
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
                    SharedPreference.addString(Constant.CUSTOMER_USER_NAME, userName)
                    SharedPreference.addString(Constant.CUSTOMER_EMAIL, email)
                    SharedPreference.addString(Constant.CUSTOMER_ID, id)
                    SharedPreference.addString(Constant.CUSTOMER_FULL_NAME, fullName)
                    SharedPreference.addString(Constant.CUSTOMER_PHONE, phone)
                    SharedPreference.addString(Constant.CUSTOMER_ADDRESS, address)
                    SharedPreference.addString(Constant.CUSTOMER_CITY, city)
                    SharedPreference.addString(Constant.CUSTOMER_STATE, state)
                    SharedPreference.addString(Constant.CUSTOMER_PINCODE, pinCode)
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