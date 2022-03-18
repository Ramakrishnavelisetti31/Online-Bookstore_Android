package com.example.bookstore.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.example.bookstore.R
import com.example.bookstore.model.Book
import com.example.bookstore.viewmodel.MyOrdersViewModel
import com.example.bookstore.viewmodel.MyOrdersViewModelFactory
import com.example.bookstore.viewmodel.SharedViewModel
import com.example.bookstore.viewmodel.SharedViewModelFactory

class FinalFragment : Fragment() {
        private lateinit var sharedViewModel: SharedViewModel
        private lateinit var myOrdersViewModel: MyOrdersViewModel
        private lateinit var goHome: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_final, container, false)

        goHome = view.findViewById(R.id.final_Button)
        sharedViewModel = ViewModelProvider(requireActivity(), SharedViewModelFactory()) [SharedViewModel::class.java]
        myOrdersViewModel = ViewModelProvider(requireActivity(), MyOrdersViewModelFactory()) [MyOrdersViewModel::class.java]
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        goHome.setOnClickListener {
            sharedViewModel.setGoToHomePageStatus(true)
        }
    }

}