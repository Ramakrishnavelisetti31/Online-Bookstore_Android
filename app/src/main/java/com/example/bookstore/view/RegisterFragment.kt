package com.example.bookstore.view

import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.bookstore.R
import com.example.bookstore.model.Customer
import com.example.bookstore.viewmodel.*

class RegisterFragment: Fragment() {
    private lateinit var emailText: EditText
    private lateinit var passwordText: EditText
    private lateinit var confirmPasswordText: EditText
    private lateinit var name: EditText
    private lateinit var mobileNumber: EditText
    private lateinit var signButton: Button
    private lateinit var goSign: TextView
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var registerViewModel: RegisterViewModel
    private lateinit var customer: Customer

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_register, container, false)

        emailText = view.findViewById(R.id.register_Email)
        passwordText = view.findViewById(R.id.register_Password)
        confirmPasswordText = view.findViewById(R.id.register_Confirm_Password)
        name = view.findViewById(R.id.register_name)
        mobileNumber = view.findViewById(R.id.register_mobile_number)
        signButton = view.findViewById(R.id.register_Button)
        goSign = view.findViewById(R.id.go_login)
        customer = Customer()
        sharedViewModel = ViewModelProvider(requireActivity(), SharedViewModelFactory())[SharedViewModel::class.java]
        registerViewModel= ViewModelProvider(requireActivity(), RegisterViewModelFactory()) [RegisterViewModel::class.java]
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        signButton.setOnClickListener { signUp() }
        goSign.setOnClickListener {  goToLoginPage() }
    }

    private fun signUp() {
        val email = emailText.text.toString()
        val password = passwordText.text.toString()
        val confirmPassword = confirmPasswordText.text.toString()
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailText.error = "Invalid email format"
        }
        else if (TextUtils.isEmpty(password)) {
            passwordText.error = "Please enter password"
        }
        else if (password.length < 6 ) {
            passwordText.error = "Password must at least 6 characters long"
        }
        else if (confirmPassword != password) {
            confirmPasswordText.error = "Password doesn't match enter again"
        }
        else {
            firebaseSignUp()
        }
    }

    private fun firebaseSignUp() {
        val fullName = name.text.toString()
        val mobileNumber = mobileNumber.text.toString()
        val email = emailText.text.toString()
        val password = passwordText.text.toString()
         customer = Customer(
             name = fullName,
             email = email,
             mobileNumber = mobileNumber,
             password = password
         )
        registerViewModel.register(customer)
        registerViewModel.registrationStatus.observe(viewLifecycleOwner, Observer {
            if (it.status) {
                sharedViewModel.setGoToHomePageStatus(true)
            }
            else {
                sharedViewModel.setGoToRegistrationPageStatus(true)
            }
        })
    }

    private fun goToLoginPage() {
        sharedViewModel.setGoToLoginPageStatus(true)
    }
}