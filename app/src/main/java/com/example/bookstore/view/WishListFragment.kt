package com.example.bookstore.view

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.bookstore.R
import com.example.bookstore.adapter.CartAdapter
import com.example.bookstore.adapter.WishListAdapter
import com.example.bookstore.model.Book
import com.example.bookstore.viewmodel.*

class WishListFragment : Fragment() {
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var wishListViewModel: WishListViewModel
    private lateinit var cancelButton: ImageButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var wishListAdapter: WishListAdapter
    private lateinit var wishList: ArrayList<Book>
    private lateinit var staggeredGridLayoutManager: StaggeredGridLayoutManager
    private lateinit var cartBooks: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_wish_list, container, false)
        cancelButton = view.findViewById(R.id.close_wishlist_fragment)
        recyclerView = view.findViewById(R.id.wishlist_recycler_view)
        cartBooks = view.findViewById(R.id.wishlistSize)
        wishList = arrayListOf()
        staggeredGridLayoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        recyclerView.layoutManager = staggeredGridLayoutManager
        recyclerView.setHasFixedSize(true)
        wishListAdapter = WishListAdapter(requireContext(), wishList)
        recyclerView.adapter = wishListAdapter
        sharedViewModel = ViewModelProvider(requireActivity(), SharedViewModelFactory())[SharedViewModel::class.java]
        wishListViewModel = ViewModelProvider(requireActivity(), WishListViewModelFactory())[WishListViewModel::class.java]

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cancelButton.setOnClickListener {
            sharedViewModel.setGoToHomePageStatus(true)
        }
        getWishlistItem()
    }

    @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
    private fun getWishlistItem() {
        wishListViewModel.getWishlist(wishList)
        wishListViewModel.getWishlistStatus.observe(viewLifecycleOwner, Observer {
            wishListAdapter.setListData(wishList)
            wishListAdapter.notifyDataSetChanged()
            val total = wishList.size.toString()
            cartBooks.text = "($total)"
        })
    }
}