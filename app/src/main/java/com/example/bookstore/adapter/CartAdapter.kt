package com.example.bookstore.adapter

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.provider.ContactsContract
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bookstore.R
import com.example.bookstore.model.Book
import com.example.bookstore.model.Customer
import com.example.bookstore.service.BookService
import com.example.bookstore.service.UserAuthService
import com.example.bookstore.view.FinalFragment
import com.example.bookstore.view.MainActivity
import com.example.bookstore.viewmodel.SharedViewModel
import com.google.firebase.auth.FirebaseAuth

class CartAdapter(private val context: Context,
private var cartList: ArrayList<Book>): RecyclerView.Adapter<CartAdapter.MyViewHolder>() {

    private var bookService = BookService()
    private var userAuthService = UserAuthService()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartAdapter.MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cart_book_layout, parent, false)
        return CartAdapter.MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartAdapter.MyViewHolder, position: Int) {
        val book: Book = cartList[position]
        holder.cartBookName.text = book.book_Name
        holder.cartBookAuthor.text = book.book_Author
        holder.cartBookPrice.text = book.book_Price
        val uri = book.book_imgUrl.toUri()
        Glide.with(context).load(uri).into(holder.cartBookImage)
        onImageClick(holder, position)
        onRemoveClick(holder, position)
        onOrderClick(holder, position)
        val isExpanded: Boolean = cartList[position].expand
        if (isExpanded) {
            holder.expandLayout.visibility = View.VISIBLE
        } else {
            holder.expandLayout.visibility = View.GONE
        }
    }

    class MyViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val cartBookImage: ImageView = view.findViewById(R.id.cart_book_image)
        val cartBookName: TextView = view.findViewById(R.id.cart_book_name)
        val cartBookAuthor: TextView = view.findViewById(R.id.cart_book_author)
        val cartBookPrice: TextView = view.findViewById(R.id.cart_book_price)
        val cartRemoveItem: ImageButton = view.findViewById(R.id.cart_remove_Button)
        val cartOrderItem: Button = view.findViewById(R.id.continue_order_Button)
        val expandLayout: ConstraintLayout = view.findViewById(R.id.expand_layout)

        val customerName: EditText = view.findViewById(R.id.c_name)
        val customerNumber: EditText = view.findViewById(R.id.c_number)
        val customerAddress: EditText = view.findViewById(R.id.c_address)
        val customerCity: EditText = view.findViewById(R.id.c_city)
        val customerState: EditText = view.findViewById(R.id.c_state)
        val customerPinCode: EditText = view.findViewById(R.id.c_pincode)
    }

    override fun getItemCount(): Int {
        return cartList.size
    }

    private fun onOrderClick(holder: CartAdapter.MyViewHolder, position: Int) {
        holder.cartOrderItem.setOnClickListener {
            val book = cartList[position]
            val name = holder.customerName.text.toString()
            val number = holder.customerNumber.text.toString()
            val address = holder.customerAddress.text.toString()
            val city = holder.customerCity.text.toString()
            val state = holder.customerState.text.toString()
            val pinCode = holder.customerPinCode.text.toString()
            val customer = Customer(fullName = name, mobileNumber = number, address = address, city = city, state = state, pinCode = pinCode)
            userAuthService.sendAddressDetails(customer)
            if (holder.customerAddress.text.isEmpty()) {
                Toast.makeText(context, "Enter address details", Toast.LENGTH_SHORT).show()
            } else {
                cartList.removeAt(position)
                bookService.removeCartItem(book.book_id, context)
                openBottomLayout(book)
            }
        }
    }

    private fun openBottomLayout(book: Book) {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.bottom_sheet_layout)

        val bottomBookImage: ImageView = dialog.findViewById(R.id.bottom_book_image)
        val bottomBookName: TextView = dialog.findViewById(R.id.bottom_book_name)
        val bottomBookAuthor: TextView = dialog.findViewById(R.id.bottom_book_author)
        val bottomBookPrice: TextView = dialog.findViewById(R.id.bottom_book_price)
        val bottomOrderItem: Button = dialog.findViewById(R.id.bottom_order_Button)

        bottomBookName.text = book.book_Name
        bottomBookAuthor.text = book.book_Author
        bottomBookPrice.text = book.book_Price
        val uri = book.book_imgUrl.toUri()
        Glide.with(context).load(uri).into(bottomBookImage)

        dialog.show()
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialog.window?.setGravity(Gravity.BOTTOM)

        bottomOrderItem.setOnClickListener {
            bookService.sentOrders(book, context)
            dialog.cancel()
            val finalFragment = FinalFragment()
            val activity = this.context as AppCompatActivity
            activity.supportFragmentManager.beginTransaction().replace(R.id.fragment_container_one, finalFragment).addToBackStack(null).commit()
        }
    }

    private fun onImageClick(holder: CartAdapter.MyViewHolder, position: Int) {
        holder.cartBookImage.setOnClickListener {
            val book = cartList[position]
                book.expand = !book.expand
            notifyItemChanged(position)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun onRemoveClick(holder: CartAdapter.MyViewHolder, position: Int) {
        holder.cartRemoveItem.setOnClickListener {
            val book = cartList.removeAt(position)
            bookService.removeCartItem(book.book_id, context)
            notifyDataSetChanged()
            Toast.makeText(context, "Book removed ", Toast.LENGTH_SHORT ).show()
        }
    }

    fun setListData(data: ArrayList<Book>) {
        cartList = data
    }

}