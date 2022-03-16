package com.example.bookstore.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bookstore.R
import com.example.bookstore.model.Book
import java.util.ArrayList

class BookAdapter(private val context: Context,
                  private var bookList: ArrayList<Book>): RecyclerView.Adapter<BookAdapter.MyViewHolder>() {

    private var allNotes = arrayListOf<Book>().apply {
        addAll(bookList)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookAdapter.MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.book_layout, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookAdapter.MyViewHolder, position: Int) {
        val book: Book = allNotes[position]
        holder.bookName.text = book.book_Name
        holder.bookAuthorName.text = book.book_Author
        holder.bookPrice.text = book.book_Price
        var uri = book.book_imgUrl.toUri()
        Glide.with(context).load(uri).into(holder.bookImage)
    }

    override fun getItemCount(): Int {
        return allNotes.size
    }

    fun setListData(data: ArrayList<Book>) {
        allNotes = data
    }

    class MyViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val bookImage: ImageView = view.findViewById(R.id.book_image)
        val bookName: TextView = view.findViewById(R.id.book_name)
        val bookAuthorName: TextView = view.findViewById(R.id.book_author)
        val bookPrice: TextView = view.findViewById(R.id.book_price)
    }

}