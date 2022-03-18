package com.example.bookstore.view

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.bookstore.R
import com.example.bookstore.adapter.MyOrdersAdapter
import com.example.bookstore.model.Book
import com.example.bookstore.viewmodel.MyOrdersViewModel
import com.example.bookstore.viewmodel.MyOrdersViewModelFactory
import com.example.bookstore.viewmodel.SharedViewModel
import com.example.bookstore.viewmodel.SharedViewModelFactory

class MyOrdersFragment : Fragment() {
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var myOrdersViewModel: MyOrdersViewModel
    private lateinit var cancelButton: ImageButton
    private lateinit var myOrdersAdapter: MyOrdersAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var orderList: ArrayList<Book>
    private lateinit var staggeredGridLayoutManager: StaggeredGridLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_my_orders, container, false)
        cancelButton = view.findViewById(R.id.close_myOrder_fragment)
        orderList = arrayListOf()
        recyclerView = view.findViewById(R.id.myOrder_recycler_view)
        staggeredGridLayoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        recyclerView.layoutManager = staggeredGridLayoutManager
        recyclerView.setHasFixedSize(true)
        myOrdersAdapter = MyOrdersAdapter(requireContext(), orderList)
        recyclerView.adapter = myOrdersAdapter
        sharedViewModel = ViewModelProvider(requireActivity(), SharedViewModelFactory()) [SharedViewModel::class.java]
        myOrdersViewModel = ViewModelProvider(requireActivity(), MyOrdersViewModelFactory()) [MyOrdersViewModel::class.java]
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cancelButton.setOnClickListener {
            sharedViewModel.setGoToHomePageStatus(true)
        }
        getOrdersList()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getOrdersList() {
        myOrdersViewModel.getOrders(orderList)
        myOrdersViewModel.getOrderStatus.observe(viewLifecycleOwner, Observer {
            myOrdersAdapter.setListData(orderList)
            myOrdersAdapter.notifyDataSetChanged()
        })
    }

}