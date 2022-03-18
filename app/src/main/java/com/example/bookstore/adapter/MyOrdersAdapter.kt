package com.example.bookstore.adapter

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.*
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bookstore.R
import com.example.bookstore.model.Book

class MyOrdersAdapter(private val context: Context,
                      private var orderList: ArrayList<Book>): RecyclerView.Adapter<MyOrdersAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyOrdersAdapter.MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.myorders_layout, parent, false)
        return MyOrdersAdapter.MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyOrdersAdapter.MyViewHolder, position: Int) {
        val book: Book = orderList[position]
        holder.orderItemId.text = book.book_id
        holder.orderBookName.text = book.book_Name
        holder.orderBookAuthor.text = book.book_Author
        holder.orderBookPrice.text = book.book_Price
        val uri = book.book_imgUrl.toUri()
        Glide.with(context).load(uri).into(holder.orderBookImage)
        onImageClick(holder, position)
        val isExpanded: Boolean = orderList[position].expand
        if (isExpanded) {
            holder.expandLayout.visibility = View.VISIBLE
        } else {
            holder.expandLayout.visibility = View.GONE
        }
    }

    class MyViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val orderItemId: TextView = view.findViewById(R.id.order_id)
        val orderBookImage: ImageView = view.findViewById(R.id.order_book_image)
        val orderBookName: TextView = view.findViewById(R.id.order_book_name)
        val orderBookAuthor: TextView = view.findViewById(R.id.order_book_author)
        val orderBookPrice: TextView = view.findViewById(R.id.order_book_price)
        val expandLayout: ConstraintLayout = view.findViewById(R.id.order_layout_expand)

    }

    override fun getItemCount(): Int {
        return orderList.size
    }

    private fun onImageClick(holder: MyOrdersAdapter.MyViewHolder, position: Int) {
        holder.orderItemId.setOnClickListener {
            val book = orderList[position]
                book.expand = !book.expand
            notifyItemChanged(position)
        }
    }

    fun setListData(data: ArrayList<Book>) {
        orderList = data
    }

}