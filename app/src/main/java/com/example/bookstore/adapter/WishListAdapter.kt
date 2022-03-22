package com.example.bookstore.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bookstore.R
import com.example.bookstore.model.Book
import com.example.bookstore.service.BookService
import com.example.bookstore.view.WishListFragment

class WishListAdapter(private val context: Context,
                      private var wishList: ArrayList<Book>): RecyclerView.Adapter<WishListAdapter.MyViewHolder>() {
    private var bookService = BookService()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WishListAdapter.MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.wishlist_book_layout, parent, false)
        return WishListAdapter.MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: WishListAdapter.MyViewHolder, position: Int) {
        val book: Book = wishList[position]
        holder.wishListBookName.text = book.book_Name
        holder.wishListBookAuthor.text = book.book_Author
        holder.wishListBookPrice.text = book.book_Price
        val uri = book.book_imgUrl.toUri()
        Glide.with(context).load(uri).into(holder.wishListBookImage)
        onOrderClick(holder, position)
        onRemoveClick(holder, position)
    }

    class MyViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val wishListBookImage: ImageView = view.findViewById(R.id.wishlist_book_image)
        val wishListBookName: TextView = view.findViewById(R.id.wishlist_book_name)
        val wishListBookAuthor: TextView = view.findViewById(R.id.wishlist_book_author)
        val wishListBookPrice: TextView = view.findViewById(R.id.wishlist_book_price)
        val wishListAddToCart: Button = view.findViewById(R.id.wishlist_add_to_cart_Button)
        val wishListRemoveItem: Button = view.findViewById(R.id.wishlist_remove_Button)
    }

    private fun onOrderClick(holder: WishListAdapter.MyViewHolder, position: Int) {
        holder.wishListAddToCart.setOnClickListener {
            val book = wishList[position]
            bookService.cartItemToFirestore(book, context)
            Toast.makeText(context, "Book added to Cart", Toast.LENGTH_SHORT ).show()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun onRemoveClick(holder: WishListAdapter.MyViewHolder, position: Int) {
        holder.wishListRemoveItem.setOnClickListener {
            val book = wishList.removeAt(position)
            bookService.removeWishlistItem(book.book_id, context)
            notifyDataSetChanged()
            Toast.makeText(context, "Book removed from Wishlist", Toast.LENGTH_SHORT ).show()
        }
    }

    override fun getItemCount(): Int {
        return wishList.size
    }

    fun setListData(data: ArrayList<Book>) {
        wishList = data
    }
}