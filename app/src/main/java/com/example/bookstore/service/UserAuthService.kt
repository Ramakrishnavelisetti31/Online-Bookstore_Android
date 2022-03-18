package com.example.bookstore.service

import android.content.ContentValues
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.bookstore.adapter.CartAdapter
import com.example.bookstore.model.AuthListener
import com.example.bookstore.model.Customer
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

    fun getDataFromFirestore(context: Context, listener: (Customer) -> Unit) {
        firestore.collection("users").document(firebaseAuth.currentUser!!.uid).get()
            .addOnCompleteListener {
                if (it.result.exists()) {
                    val userNameResult = it.result.getString("userName").toString()
                    val emailResult = it.result.getString("email").toString()
                    val id = firebaseAuth.currentUser!!.uid
                        val customer = Customer(userName = userNameResult, email = emailResult, customer_id = id)
                        listener(customer)
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