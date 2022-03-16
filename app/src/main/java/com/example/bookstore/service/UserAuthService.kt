package com.example.bookstore.service

import android.content.ContentValues
import android.content.Context
import android.util.Log
import androidx.core.net.toUri
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
        val customerDB: MutableMap<String, Any> = HashMap()
        customerDB[NAME] = customer.name
        customerDB[EMAIL] = customer.email
        customerDB[MOBILE] = customer.mobileNumber
        customerDB[PASSWORD] = customer.password
        firebaseAuth.currentUser?.let {
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

    fun getDataFromFirestore(context: Context, listener: (AuthListener) -> Unit) {
        firestore.collection("users").document(firebaseAuth.currentUser!!.uid).get()
            .addOnCompleteListener {
                if (it.result.exists()) {
                    var nameResult = it.result.getString("name").toString()
                    var emailResult = it.result.getString("email").toString()
                    var mobileResult = it.result.getString("mobile_number").toString()
                    SharedPreference.initSharedPreferences(context)
                    SharedPreference.addString(Constant.CUSTOMER_NAME, nameResult)
                    SharedPreference.addString(Constant.CUSTOMER_EMAIL, emailResult)
                    SharedPreference.addString(Constant.CUSTOMER_MOBILE_NUMBER, mobileResult)
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

    companion object {
        const val NAME = "name"
        const val EMAIL = "email"
        const val PASSWORD = "password"
        const val MOBILE = "mobile_number"
    }
}