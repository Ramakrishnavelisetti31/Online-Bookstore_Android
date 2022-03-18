package com.example.bookstore.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.bookstore.R
import com.example.bookstore.viewmodel.SharedViewModel
import com.example.bookstore.viewmodel.SharedViewModelFactory

class MainActivity : AppCompatActivity() {
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var fragmentContainerView: FragmentContainerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fragmentContainerView = findViewById(R.id.fragment_container_one)
        sharedViewModel = ViewModelProvider(this, SharedViewModelFactory())[SharedViewModel::class.java]
        sharedViewModel.setGoToLoginPageStatus(true)
        observeAppNav()
    }

    private fun observeAppNav() {
        sharedViewModel.goToLoginPageStatus.observe(this, Observer {
            if (it == true) {
                supportFragmentManager.beginTransaction().apply {
                    replace(R.id.fragment_container_one, LoginFragment()).addToBackStack(null).commit()
                }
            }
        })

        sharedViewModel.goToRegistrationPageStatus.observe(this, Observer {
            if (it == true) {
                supportFragmentManager.beginTransaction().apply {
                    replace(R.id.fragment_container_one, RegisterFragment()).addToBackStack(null).commit()
                }
            }
        })

        sharedViewModel.goToHomePageStatus.observe(this, Observer {
            if (it == true) {
                supportFragmentManager.beginTransaction().apply {
                    replace(R.id.fragment_container_one, HomeFragment()).addToBackStack(null).commit()
                }
            }
        })

        sharedViewModel.goToProfilePageStatus.observe(this, Observer {
            if (it == true) {
                supportFragmentManager.beginTransaction().apply {
                    replace(R.id.fragment_container_one, ProfileFragment()).addToBackStack(null).commit()
                }
            }
        })

        sharedViewModel.goToCartPageStatus.observe(this, Observer {
            if (it == true) {
                supportFragmentManager.beginTransaction().apply {
                    replace(R.id.fragment_container_one, CartFragment()).addToBackStack(null).commit()
                }
            }
        })

        sharedViewModel.goToWishListPageStatus.observe(this, Observer {
            if (it == true) {
                supportFragmentManager.beginTransaction().apply {
                    replace(R.id.fragment_container_one, WishListFragment()).addToBackStack(null).commit()
                }
            }
        })

        sharedViewModel.goToMyOrderPageStatus.observe(this, Observer {
            if (it == true) {
                supportFragmentManager.beginTransaction().apply {
                    replace(R.id.fragment_container_one, MyOrdersFragment()).addToBackStack(null).commit()
                }
            }
        })

        sharedViewModel.goToFinalPageStatus.observe(this, Observer {
            if (it == true) {
                supportFragmentManager.beginTransaction().apply {
                    replace(R.id.fragment_container_one, FinalFragment()).addToBackStack(null).commit()
                }
            }
        })

    }

}