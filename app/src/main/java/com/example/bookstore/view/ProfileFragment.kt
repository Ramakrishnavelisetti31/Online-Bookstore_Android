package com.example.bookstore.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.bookstore.R
import com.example.bookstore.model.Constant
import com.example.bookstore.model.Customer
import com.example.bookstore.model.SharedPreference
import com.example.bookstore.viewmodel.ProfileViewModel
import com.example.bookstore.viewmodel.ProfileViewModelFactory
import com.example.bookstore.viewmodel.SharedViewModel
import com.example.bookstore.viewmodel.SharedViewModelFactory

class ProfileFragment : Fragment() {
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var cancelButton: ImageButton
    private lateinit var customerEmail: TextView
    private lateinit var customerName: TextView
    private lateinit var customerMobile: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        customerEmail = view.findViewById(R.id.email_viewer)
        customerName = view.findViewById(R.id.name_viewer)
        customerMobile = view.findViewById(R.id.mobile_viewer)
        cancelButton = view.findViewById(R.id.close_profile_fragment)
        profileViewModel = ViewModelProvider(requireActivity(), ProfileViewModelFactory()) [ProfileViewModel::class.java]
        sharedViewModel = ViewModelProvider(requireActivity(), SharedViewModelFactory())[SharedViewModel::class.java]
        profileViewModel.getData(requireContext())
        SharedPreference.initSharedPreferences(requireContext())
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        displayUserData()
        cancelButton.setOnClickListener {
            sharedViewModel.setGoToHomePageStatus(true)
        }
    }

    private fun displayUserData() {
        customerName.text = SharedPreference.getString(Constant.CUSTOMER_NAME)
        customerEmail.text = SharedPreference.getString(Constant.CUSTOMER_EMAIL)
        customerMobile.text = SharedPreference.getString(Constant.CUSTOMER_MOBILE_NUMBER)
    }
}