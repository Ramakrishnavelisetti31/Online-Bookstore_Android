package com.example.bookstore.adapter

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bookstore.R
import com.example.bookstore.model.Book
import com.example.bookstore.model.Constant
import com.example.bookstore.model.Customer
import com.example.bookstore.model.SharedPreference
import com.example.bookstore.service.BookService
import com.example.bookstore.service.UserAuthService
import com.example.bookstore.view.FinalFragment

class CartAdapter(private val context: Context,
private var cartList: ArrayList<Book>): RecyclerView.Adapter<CartAdapter.MyViewHolder>() {

    private var bookService = BookService()
    private var userAuthService = UserAuthService()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartAdapter.MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cart_book_layout, parent, false)
        return CartAdapter.MyViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: CartAdapter.MyViewHolder, position: Int) {
        val book: Book = cartList[position]
        val isExpanded: Boolean = cartList[position].expand
        val bName = book.book_Name
        val aName = book.book_Author
        val img = book.book_imgUrl
        var price = book.book_Price
        holder.cartBookName.text = bName
        holder.cartBookAuthor.text = aName
        holder.cartBookPrice.text = price
        val uri = img.toUri()
        Glide.with(context).load(uri).into(holder.cartBookImage)
        var item = 1
        holder.cartItem.text = item.toString()
        holder.increaseItem.setOnClickListener {
            item++
            holder.cartItem.text = item.toString()
             price = Math.multiplyExact(item, book.book_Price.toInt()).toString()
            holder.cartBookPrice.text = price
            onOrderClick(book, holder, position, bName, aName, img, price)
        }

        holder.decreaseItem.setOnClickListener {
           if (item > 1) {
               item--
               holder.cartItem.text = item.toString()
               price = Math.multiplyExact(item, book.book_Price.toInt()).toString()
               holder.cartBookPrice.text = price
               onOrderClick(book, holder, position, bName, aName, img, price)
           } else {
               holder.cartItem.text = item.toString()
           }
        }

        onOrderClick(book, holder, position, bName, aName, img, price)
        onImageClick(holder.cartBookImage, holder, position, book)
        onRemoveClick(holder.cartRemoveItem, position, book)
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
        val increaseItem: ImageButton = view.findViewById(R.id.increase_item)
        val decreaseItem: ImageButton = view.findViewById(R.id.decrease_item)
        val cartItem: TextView = view.findViewById(R.id.item_count)

        val expandLayout: ConstraintLayout = view.findViewById(R.id.expand_layout)
        val customerName: EditText = view.findViewById(R.id.c_name)
        val customerNumber: EditText = view.findViewById(R.id.c_number)
        val customerAddress: EditText = view.findViewById(R.id.c_address)
        val customerCity: EditText = view.findViewById(R.id.c_city)
        val customerState: EditText = view.findViewById(R.id.c_state)
        val customerPinCode: EditText = view.findViewById(R.id.c_pincode)
        val cartOrderItem: Button = view.findViewById(R.id.continue_order_Button)
    }

    override fun getItemCount(): Int {
        return cartList.size
    }

    private fun onOrderClick(
        book: Book,
        holder: MyViewHolder,
        position: Int,
        bName: String,
        aName: String,
        img: String,
        price: String
    ) {
        holder.cartOrderItem.setOnClickListener {
            if (holder.customerAddress.text.isNullOrEmpty()) {
                val name = holder.customerName.text.toString()
                val number = holder.customerNumber.text.toString()
                val address = holder.customerAddress.text.toString()
                val city = holder.customerCity.text.toString()
                val state = holder.customerState.text.toString()
                val pinCode = holder.customerPinCode.text.toString()
                val customer = Customer(
                    fullName = name,
                    mobileNumber = number,
                    address = address,
                    city = city,
                    state = state,
                    pinCode = pinCode
                )
                userAuthService.sendAddressDetails(customer)
                openBottomLayout(bName, aName, img, price)
                cartList.removeAt(position)
                bookService.removeCartItem(book.book_id, context)
            }
            else {
                openBottomLayout(bName, aName, img, price)
                cartList.removeAt(position)
                bookService.removeCartItem(book.book_id, context)
            }
        }
    }

    private fun openBottomLayout(bName: String, aName: String, img: String, price: String) {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.bottom_sheet_layout)

        val bottomBookImage: ImageView = dialog.findViewById(R.id.bottom_book_image)
        val bottomBookName: TextView = dialog.findViewById(R.id.bottom_book_name)
        val bottomBookAuthor: TextView = dialog.findViewById(R.id.bottom_book_author)
        val bottomBookPrice: TextView = dialog.findViewById(R.id.bottom_book_price)
        val bottomOrderItem: Button = dialog.findViewById(R.id.bottom_order_Button)

        bottomBookName.text = bName
        bottomBookAuthor.text = aName
        bottomBookPrice.text = price
        val uri = img.toUri()
        Glide.with(context).load(uri).into(bottomBookImage)
        dialog.show()
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialog.window?.setGravity(Gravity.BOTTOM)

        val book = Book(book_Name = bName, book_Author = aName, book_Price = price, book_imgUrl = img)
        bottomOrderItem.setOnClickListener {
            bookService.sentOrders(book, context)
            dialog.cancel()
            val finalFragment = FinalFragment()
            val activity = this.context as AppCompatActivity
            activity.supportFragmentManager.beginTransaction().replace(R.id.fragment_container_one, finalFragment).addToBackStack(null).commit()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun onImageClick(imageView: ImageView, holder: MyViewHolder, position: Int, book: Book) {
        imageView.setOnClickListener {
            cartList[position]
            book.expand = !book.expand
            notifyItemChanged(position)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun onRemoveClick(imageButton: ImageButton, position: Int, book: Book) {
        imageButton.setOnClickListener {
            cartList.removeAt(position)
            bookService.removeCartItem(book.book_id, context)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, cartList.size)
            Toast.makeText(context, "Book removed ", Toast.LENGTH_SHORT ).show()
        }
    }

    fun setListData(data: ArrayList<Book>) {
        cartList = data
    }

}