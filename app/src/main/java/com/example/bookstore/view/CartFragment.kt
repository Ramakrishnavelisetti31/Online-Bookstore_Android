package com.example.bookstore.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.lifecycle.ViewModelProvider
import com.example.bookstore.R
import com.example.bookstore.viewmodel.SharedViewModel
import com.example.bookstore.viewmodel.SharedViewModelFactory

class CartFragment : Fragment() {

    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var cancelButton: ImageButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_cart, container, false)
        cancelButton = view.findViewById(R.id.close_cart_fragment)
        sharedViewModel = ViewModelProvider(requireActivity(), SharedViewModelFactory())[SharedViewModel::class.java]

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cancelButton.setOnClickListener {
            sharedViewModel.setGoToHomePageStatus(true)
        }
    }

}