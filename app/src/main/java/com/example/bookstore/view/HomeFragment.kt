package com.example.bookstore.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.view.*
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bookstore.R
import com.example.bookstore.adapter.BookAdapter
import com.example.bookstore.model.Book
import com.example.bookstore.model.Constant
import com.example.bookstore.model.SharedPreference
import com.example.bookstore.viewmodel.*
import com.google.android.material.navigation.NavigationView
import kotlin.collections.ArrayList

class HomeFragment : Fragment(){
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    private lateinit var toolbar: Toolbar
    private lateinit var searchView: androidx.appcompat.widget.SearchView
    private lateinit var recyclerView: RecyclerView
    private lateinit var bookAdapter: BookAdapter
    private lateinit var bookList: ArrayList<Book>
    private lateinit var gridLayoutManager: GridLayoutManager
    private lateinit var totalBooks: TextView
    private lateinit var spinner: Spinner
    private lateinit var progressBar: ProgressBar
    var page = 1
    var isLoading = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        drawerLayout = view.findViewById(R.id.drawer_layout)
        navigationView = view.findViewById(R.id.navigation_view)
        toolbar = view.findViewById(R.id.tool_bar)
        searchView = view.findViewById(R.id.search_books)
        recyclerView = view.findViewById(R.id.recycler_view)
        totalBooks = view.findViewById(R.id.bookSize)
        spinner = view.findViewById(R.id.price_spinner)
        progressBar = view.findViewById(R.id.progress_bar)
        bookList = arrayListOf()
        val activity = activity as AppCompatActivity?
        activity?.setSupportActionBar(toolbar)
        activity?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        activity?.supportActionBar?.setDisplayShowTitleEnabled(false)
        actionBarDrawerToggle = ActionBarDrawerToggle(requireActivity(), drawerLayout, R.string.menu_open, R.string.menu_close)
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.drawerArrowDrawable.color = resources.getColor(R.color.white)
        actionBarDrawerToggle.syncState()
        gridLayoutManager = GridLayoutManager(requireContext(), 2)
        recyclerView.layoutManager = gridLayoutManager
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
        searchNote()
        filter()
        setHasOptionsMenu(true)
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val visibleItemCount = gridLayoutManager.childCount
                val firstItemPosition = gridLayoutManager.findFirstCompletelyVisibleItemPosition()+1
                val total = gridLayoutManager.itemCount
                if (isLoading) {
                    if ((visibleItemCount + firstItemPosition >= total)) {
                        page ++
                        pagination()
                    }
                }
            }
        })
    }

    @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
    private fun viewBooks() {
        homeViewModel.getBooks(bookList, requireContext())
        homeViewModel.getBookStatus.observe(viewLifecycleOwner, Observer {
            bookAdapter.setListData(bookList)
            bookAdapter.notifyDataSetChanged()
            val total = bookAdapter.itemCount
            totalBooks.text = "($total)"
        })
    }

    private fun pagination() {
        isLoading = true
        progressBar.visibility = View.VISIBLE
        viewBooks()
        Handler().postDelayed({
            isLoading = false
            progressBar.visibility = View.GONE
        }, 3000)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.menu_wishlist -> {
                sharedViewModel.setGoToWishListPageStatus(true)
            }
            R.id.menu_cart -> {
                sharedViewModel.setGoToCartPageStatus(true)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun filter() {
        val items = ArrayList<String>()
        items.add(getString(R.string.sort_by_relevance))
        items.add(getString(R.string.low_to_high))
        items.add(getString(R.string.high_to_low))

        val adapter: ArrayAdapter<String> =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, items)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                if (spinner.selectedItemPosition.toString() == "0") {
                    bookAdapter.setListData(bookList)
                    bookAdapter.notifyDataSetChanged()
                }
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
