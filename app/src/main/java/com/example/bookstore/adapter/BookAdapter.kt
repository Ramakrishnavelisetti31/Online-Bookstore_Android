package com.example.bookstore.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bookstore.R
import com.example.bookstore.model.Book
import com.example.bookstore.service.BookService
import java.util.*
import kotlin.collections.ArrayList

class BookAdapter(private val context: Context,
                  private var bookList: ArrayList<Book>): RecyclerView.Adapter<BookAdapter.MyViewHolder>(), Filterable {

    private var bookService = BookService()
    private var allBooks = arrayListOf<Book>().apply {
        addAll(bookList)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookAdapter.MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.book_layout, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookAdapter.MyViewHolder, position: Int) {
        val book: Book = allBooks[position]
        holder.bookName.text = book.book_Name
        holder.bookAuthorName.text = book.book_Author
        holder.bookPrice.text = book.book_Price
        val uri = book.book_imgUrl.toUri()
        Glide.with(context).load(uri).into(holder.bookImage)
        onCartClick(holder, position)
        onWishListClick(holder, position)
    }

    override fun getItemCount(): Int {
        return allBooks.size
    }

    fun setListData(data: ArrayList<Book>) {
        allBooks = data
    }

    class MyViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val bookImage: ImageView = view.findViewById(R.id.book_image)
        val bookName: TextView = view.findViewById(R.id.book_name)
        val bookAuthorName: TextView = view.findViewById(R.id.book_author)
        val bookPrice: TextView = view.findViewById(R.id.book_price)
        val addToCart: Button = view.findViewById(R.id.add_to_cart_button)
        val addToWishList: ImageButton = view.findViewById(R.id.add_to_wishlist_button)
    }

    @SuppressLint("SetTextI18n")
    private fun onCartClick(holder: MyViewHolder, position: Int) {
        holder.addToCart.setOnClickListener {
            val book = bookList[position]
            bookService.cartItemToFirestore(book, context)
            holder.addToCart.text = "ADDED TO CART"
            Toast.makeText(context, "Book added to Cart", Toast.LENGTH_SHORT ).show()
        }
    }

    private fun onWishListClick(holder: MyViewHolder, position: Int) {
        holder.addToWishList.setOnClickListener {
            val book = bookList[position]
            bookService.wishListItemToFirestore(book, context)
            holder.addToWishList.setImageResource(R.drawable.ic_vector_wishlist2_24)
        }
    }

    fun priceLowToHigh(): ArrayList<Book> {
        val sortedList = bookList.sortedBy { book -> book.book_Price.toInt() }
       return sortedList.toCollection(ArrayList())
    }

    fun priceHighToLow(): ArrayList<Book> {
        val sortedList = bookList.sortedByDescending { book -> book.book_Price.toInt() }
        return sortedList.toCollection(ArrayList())
    }

    override fun getFilter(): Filter {
        return myFilter
    }

    var myFilter: Filter = object : Filter() {
        override fun performFiltering(charSequence: CharSequence?): FilterResults {
            val filteredList: ArrayList<Book> = arrayListOf()
            if (charSequence.toString().isEmpty()) {
                filteredList.addAll(allBooks)
            } else {
                for (book in allBooks) {
                    if (book.book_Name.lowercase(Locale.ROOT).contains(charSequence.toString()
                            .lowercase(Locale.getDefault()))) {
                        filteredList.add(book)
                    }
                }
            }
            val filterResults = FilterResults()
            filterResults.values = filteredList
            return filterResults
        }
        @SuppressLint("NotifyDataSetChanged")
        override fun publishResults(charSequence: CharSequence?, filterResults: FilterResults) {
            bookList.clear()
            bookList.addAll(filterResults.values as Collection<Book>)
            notifyDataSetChanged()
        }
    }

}