package com.example.bookstore.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.bookstore.R
import com.example.bookstore.adapter.BookAdapter
import com.example.bookstore.model.Book
import com.example.bookstore.viewmodel.HomeViewModel
import com.example.bookstore.viewmodel.HomeViewModelFactory
import com.example.bookstore.viewmodel.SharedViewModel
import com.example.bookstore.viewmodel.SharedViewModelFactory
import com.google.android.material.navigation.NavigationView

class HomeFragment : Fragment() {
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    private lateinit var toolbar: Toolbar
    private lateinit var searchView: androidx.appcompat.widget.SearchView
    private lateinit var cartButton: ImageButton
    private lateinit var wishlistButton: ImageButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var bookAdapter: BookAdapter
    private lateinit var bookList: ArrayList<Book>
    private lateinit var staggeredGridLayoutManager: StaggeredGridLayoutManager
    private lateinit var totalBooks: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        drawerLayout = view.findViewById(R.id.drawer_layout)
        navigationView = view.findViewById(R.id.navigation_view)
        toolbar = view.findViewById(R.id.tool_bar)
        searchView = view.findViewById(R.id.search_notes)
        cartButton = view.findViewById(R.id.cart)
        wishlistButton = view.findViewById(R.id.wishlist)
        recyclerView = view.findViewById(R.id.recycler_view)
        totalBooks = view.findViewById(R.id.bookSize)
        bookList = arrayListOf()
        val activity = activity as AppCompatActivity?
        activity?.setSupportActionBar(toolbar)
        activity?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        activity?.supportActionBar?.setDisplayShowTitleEnabled(false)
        actionBarDrawerToggle = ActionBarDrawerToggle(requireActivity(), drawerLayout, R.string.menu_open, R.string.menu_close)
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.drawerArrowDrawable.color = resources.getColor(R.color.white)
        actionBarDrawerToggle.syncState()
        staggeredGridLayoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        recyclerView.layoutManager = staggeredGridLayoutManager
        recyclerView.setHasFixedSize(true)
        bookAdapter = BookAdapter(requireContext(), bookList)
        recyclerView.adapter = bookAdapter
        sharedViewModel = ViewModelProvider(requireActivity(), SharedViewModelFactory())[SharedViewModel::class.java]
        homeViewModel = ViewModelProvider(requireActivity(), HomeViewModelFactory())[HomeViewModel::class.java]
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navigationMenu()
        viewBooks()
    }

    @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
    private fun viewBooks() {
        homeViewModel.getBooks(bookList)
        homeViewModel.getBookStatus.observe(viewLifecycleOwner, Observer {
            bookAdapter.setListData(bookList)
            bookAdapter.notifyDataSetChanged()
            val total = bookList.size.toString()
            totalBooks.text = "($total)"
        })
    }

    private fun navigationMenu() {
        navigationView.setNavigationItemSelectedListener {
            it.isChecked = true
            when(it.itemId) {
                R.id.nav_MyAccount -> {
                    sharedViewModel.setGoToProfilePageStatus(true)
                }
                R.id.nav_myCart -> {
                    sharedViewModel.setGoToCartPageStatus(true)
                }
                R.id.nav_myWishlist -> {
                    sharedViewModel.setGoToWishListPageStatus(true)
                }
                R.id.nav_logOut -> {
                    logOut()
                }
            }
            drawerLayout.closeDrawers()
            true
        }
    }

    private fun logOut() {
        homeViewModel.signOut()
        homeViewModel.logoutStatus.observe(viewLifecycleOwner, Observer {
            if (it.status) {
                sharedViewModel.setGoToLoginPageStatus(true)
                Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

}
