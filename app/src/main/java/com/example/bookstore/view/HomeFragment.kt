package com.example.bookstore.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.bookstore.R
import com.example.bookstore.adapter.BookAdapter
import com.example.bookstore.model.AuthListener
import com.example.bookstore.model.Book
import com.example.bookstore.viewmodel.*
import com.google.android.material.navigation.NavigationView
import java.util.*
import kotlin.collections.ArrayList

class HomeFragment : Fragment() {
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var cartViewModel: CartViewModel
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
    private lateinit var spinner: Spinner

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        drawerLayout = view.findViewById(R.id.drawer_layout)
        navigationView = view.findViewById(R.id.navigation_view)
        toolbar = view.findViewById(R.id.tool_bar)
        searchView = view.findViewById(R.id.search_books)
        cartButton = view.findViewById(R.id.cart)
        wishlistButton = view.findViewById(R.id.wishlist)
        recyclerView = view.findViewById(R.id.recycler_view)
        totalBooks = view.findViewById(R.id.bookSize)
        spinner = view.findViewById(R.id.price_spinner)
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
        cartViewModel = ViewModelProvider(requireActivity(), CartViewModelFactory())[CartViewModel::class.java]
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navigationMenu()
            viewBooks()
        searchNote()
        filter()

        cartButton.setOnClickListener {
            sharedViewModel.setGoToCartPageStatus(true)
        }
        wishlistButton.setOnClickListener {
            sharedViewModel.setGoToWishListPageStatus(true)
        }
    }

    @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
    private fun viewBooks() {
        homeViewModel.getBooks(bookList)
        homeViewModel.getBookStatus.observe(viewLifecycleOwner, Observer {
            bookAdapter.setListData(bookList)
            bookAdapter.notifyDataSetChanged()
            val total = bookAdapter.itemCount.toString()
            totalBooks.text = "($total)"
        })
    }

    private fun filter() {
        val items = ArrayList<String>()
        items.add("Sort by relevance")
        items.add("Price: Low to High")
        items.add("Price High to Low")

        val adapter: ArrayAdapter<String> =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, items)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (spinner.selectedItemPosition.toString() == "1") {

                    val list = bookAdapter.priceLowToHigh()
                    bookAdapter.setListData(list)
                    bookAdapter.notifyDataSetChanged()

                } else if (spinner.selectedItemPosition.toString() == "2") {

                    val list = bookAdapter.priceHighToLow()
                    bookAdapter.setListData(list)
                    bookAdapter.notifyDataSetChanged()

                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }
    }

    private fun navigationMenu() {
        navigationView.setNavigationItemSelectedListener {
            it.isChecked = true
            when(it.itemId) {
                R.id.nav_myOrders -> {
                    sharedViewModel.setGoToMyOrderPageStatus(true)
                }
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

    private fun searchNote() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String): Boolean {
                bookAdapter.filter.filter(query)
                return false
            }
            @SuppressLint("NotifyDataSetChanged")
            override fun onQueryTextChange(newText: String): Boolean {
                bookAdapter.filter.filter(newText)
                return false
            }
        })
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
