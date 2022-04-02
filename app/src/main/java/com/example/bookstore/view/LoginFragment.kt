package com.example.bookstore.view

import android.database.Cursor
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.bookstore.R
import com.example.bookstore.model.Customer
import com.example.bookstore.viewmodel.*

class LoginFragment : Fragment() {
    private lateinit var emailText: EditText
    private lateinit var passwordText: EditText
    private lateinit var loginButton: Button
    private lateinit var goRegister: TextView
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var customer: Customer
    private lateinit var networkConnectivity: NetworkConnectivity
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)
        emailText = view.findViewById(R.id.logIn_Email)
        passwordText = view.findViewById(R.id.logIn_Password)
        loginButton = view.findViewById(R.id.logIn_Button)
        goRegister = view.findViewById(R.id.go_register)
        customer = Customer()
        sharedViewModel = ViewModelProvider(requireActivity(), SharedViewModelFactory())[SharedViewModel::class.java]
        loginViewModel = ViewModelProvider(requireActivity(), LoginViewModelFactory()) [LoginViewModel::class.java]
        networkConnectivity = NetworkConnectivity(requireContext())
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loginButton.setOnClickListener { login() }
        goRegister.setOnClickListener { goToSignUpPage() }

    }

    private fun login() {
        val email = emailText.text.toString()
        val password = passwordText.text.toString()
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailText.error = "Invalid email format"
        } else if (TextUtils.isEmpty(password)) {
            passwordText.error = "Please enter password"
        } else {
            fireBaseLogin()
        }
    }

    private fun fireBaseLogin() {
        val email = emailText.text.toString().trim()
        val password = passwordText.text.toString().trim()
        customer = Customer(email = email, password = password)
        networkConnectivity.observe(viewLifecycleOwner, Observer {isAvailable ->
            if (isAvailable) {
                loginViewModel.logIn(customer)
//        loginViewModel.apiLogIn(customer)
            } else {
                loginViewModel.localLogIn(customer, requireContext())
            }
        })
        loginViewModel.loginStatus.observe(viewLifecycleOwner, Observer {
            if (it.status) {
                sharedViewModel.setGoToHomePageStatus(true)
                Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
            }
            else {
                sharedViewModel.setGoToLoginPageStatus(true)
                Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun goToSignUpPage() {
        sharedViewModel.setGoToRegistrationPageStatus(true)
    }
}