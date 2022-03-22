package com.example.bookstore.view

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.example.bookstore.R
import com.example.bookstore.adapter.CartAdapter
import com.example.bookstore.model.Book
import com.example.bookstore.viewmodel.*

class CartFragment : Fragment() {
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var cartViewModel: CartViewModel
    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var cancelButton: ImageButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var cartAdapter: CartAdapter
    private lateinit var cartList: ArrayList<Book>
    private lateinit var staggeredGridLayoutManager: StaggeredGridLayoutManager
    private lateinit var cartBooks: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_cart, container, false)
        cancelButton = view.findViewById(R.id.close_cart_fragment)
        recyclerView = view.findViewById(R.id.cart_recycler_view)
        cartBooks = view.findViewById(R.id.cartSize)
        cartList = arrayListOf()
        staggeredGridLayoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        recyclerView.layoutManager = staggeredGridLayoutManager
        recyclerView.setHasFixedSize(true)
        cartAdapter = CartAdapter(requireContext(), cartList)
        recyclerView.adapter = cartAdapter
        sharedViewModel = ViewModelProvider(requireActivity(), SharedViewModelFactory())[SharedViewModel::class.java]
        cartViewModel = ViewModelProvider(requireActivity(), CartViewModelFactory())[CartViewModel::class.java]
        profileViewModel = ViewModelProvider(requireActivity(), ProfileViewModelFactory())[ProfileViewModel::class.java]
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cancelButton.setOnClickListener {
            sharedViewModel.setGoToHomePageStatus(true)
        }
        getCartItem()
    }

    @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
    private fun getCartItem() {
        cartViewModel.getCart(cartList)
        cartViewModel.getCartStatus.observe(viewLifecycleOwner, Observer {
            cartAdapter.setListData(cartList)
            cartListSize()
            cartAdapter.notifyDataSetChanged()
        })
    }

    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    private fun cartListSize() {
        val total = cartAdapter.itemCount
        if (total > 0) {
            cartBooks.text = "($total)"
            sharedViewModel.getCount(total)
        }
        cartAdapter.notifyDataSetChanged()
    }

}