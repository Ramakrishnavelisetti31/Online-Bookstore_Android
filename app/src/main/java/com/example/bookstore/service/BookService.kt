package com.example.bookstore.service

import android.content.ContentValues
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.core.net.toUri
import com.example.bookstore.database.BookDatabase
import com.example.bookstore.model.AuthListener
import com.example.bookstore.model.Book
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class BookService {
    private var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private var firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private lateinit var documentReference: DocumentReference
    private lateinit var collectionReference: CollectionReference
    private lateinit var bookDatabase: BookDatabase

    fun getBookDataFromFirestore(bookList: ArrayList<Book>,context: Context, listener: (AuthListener) -> Unit) {
        firestore.collection("books").get()
            .addOnSuccessListener { list ->
                for (document in list) {
                    val name = document.getString("book_name").toString()
                    val authorName = document.getString("book_author").toString()
                    val price = document.getString("book_price").toString()
                    val img_url = document.getString("book_imgurl")?.toUri().toString()
                    val id = document.id
                    val book = Book(name, authorName, price, img_url)
                    bookList.add(book)
                    sqlData(bookList, book, id, context)
                    bookDatabase = BookDatabase(context)
                    bookDatabase.getBook()
                    Log.d("BookService", "${bookList.size}")
                    listener(AuthListener(true, "Fetch books successfully"))
                }
            }
    }

    private fun sqlData(bookList: ArrayList<Book>, book: Book, id: String, context: Context) {
        bookDatabase = BookDatabase(context)
        val count = bookDatabase.bookCount()
        if (count != bookList.size) {
            val res = bookDatabase.existItem(id)
            if (res == false) {
                bookDatabase.saveBook(book)
            }
        }
    }

    fun cartItemToFirestore(book: Book, context: Context) {
        firebaseAuth.currentUser?.let {
            documentReference =
                firestore.collection("users").document(it.uid).collection("cartBook").document()
            book.book_id = documentReference.id
            book.user_id = it.uid
            val bookDB: MutableMap<String, Any> = HashMap()
            bookDB["book_name"] = book.book_Name
            bookDB["book_author"] = book.book_Author
            bookDB["book_price"] = book.book_Price
            bookDB["book_imgurl"] = book.book_imgUrl
            bookDB["book_id"] = book.book_id
            bookDB["user_id"] = book.user_id
            bookDatabase = BookDatabase(context)
            bookDatabase.saveCart(book)
            documentReference.set(bookDB).addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(context, "Book added to Cart", Toast.LENGTH_SHORT ).show()
                } else {
                    Log.d(ContentValues.TAG, "get failed with ", it.exception)
                }
            }
        }
    }

    fun wishListItemToFirestore(book: Book, context: Context) {
        firebaseAuth.currentUser?.let {
            documentReference =
                firestore.collection("users").document(it.uid).collection("wishList").document()
            book.book_id = documentReference.id
            book.user_id = it.uid
            val bookDB: MutableMap<String, Any> = HashMap()
            bookDB["book_name"] = book.book_Name
            bookDB["book_author"] = book.book_Author
            bookDB["book_price"] = book.book_Price
            bookDB["book_imgurl"] = book.book_imgUrl
            bookDB["book_id"] = book.book_id
            bookDB["user_id"] = book.user_id
            bookDatabase = BookDatabase(context)
            bookDatabase.saveWish(book)
            documentReference.set(bookDB).addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(context, "Book added to Wishlist", Toast.LENGTH_SHORT ).show()
                } else {
                    Log.d(ContentValues.TAG, "get failed with ", it.exception)
                }
            }
        }
    }

    fun sentOrders(book: Book, context: Context) {
        firebaseAuth.currentUser?.let {
            documentReference =
                firestore.collection("users").document(it.uid).collection("myOrders").document()
            book.book_id = documentReference.id
            book.user_id = it.uid
            val bookDB: MutableMap<String, Any> = HashMap()
            bookDB["book_name"] = book.book_Name
            bookDB["book_author"] = book.book_Author
            bookDB["book_price"] = book.book_Price
            bookDB["book_imgurl"] = book.book_imgUrl
            bookDB["book_id"] = book.book_id
            bookDB["user_id"] = book.user_id
            documentReference.set(bookDB).addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(context, "Book added to Wishlist", Toast.LENGTH_SHORT ).show()
                } else {
                    Log.d(ContentValues.TAG, "get failed with ", it.exception)
                }
            }
        }
    }

    fun getMyOrders(bookList: ArrayList<Book>, listener: (AuthListener) -> Unit) {
        firestore.collection("users").document(firebaseAuth.currentUser!!.uid).collection("myOrders")
            .get().addOnSuccessListener { list ->
                for (document in list) {
                    val name = document.getString("book_name").toString()
                    val authorName = document.getString("book_author").toString()
                    val price = document.getString("book_price").toString()
                    val img_url = document.getString("book_imgurl")?.toUri().toString()
                    val id = document.id
                    val book = Book(name, authorName, price, img_url, id)
                    bookList.add(book)
                    listener(AuthListener(true, "Fetch books successfully"))
                }
            }
    }

    fun getCartItem(bookList: ArrayList<Book>, context: Context, listener: (AuthListener) -> Unit) {
        firestore.collection("users").document(firebaseAuth.currentUser!!.uid).collection("cartBook")
            .get().addOnSuccessListener { list ->
                bookDatabase = BookDatabase(context)
                bookDatabase.getCart()
                for (document in list) {
                    val name = document.getString("book_name").toString()
                    val authorName = document.getString("book_author").toString()
                    val price = document.getString("book_price").toString()
                    val img_url = document.getString("book_imgurl")?.toUri().toString()
                    val id = document.id
                    val book = Book(name, authorName, price, img_url, id)
                    bookList.add(book)
                    listener(AuthListener(true, "Fetch books successfully"))
                }
            }
    }

    fun getWishlistItem(wishlist: ArrayList<Book>, context: Context, listener: (AuthListener) -> Unit) {
        firestore.collection("users").document(firebaseAuth.currentUser!!.uid).collection("wishList")
            .get().addOnSuccessListener { list ->
                bookDatabase = BookDatabase(context)
                bookDatabase.getWish()
                for (document in list) {
                    val name = document.getString("book_name").toString()
                    val authorName = document.getString("book_author").toString()
                    val price = document.getString("book_price").toString()
                    val img_url = document.getString("book_imgurl")?.toUri().toString()
                    val id = document.id
                    val book = Book(name, authorName, price, img_url, id)
                    wishlist.add(book)
                    listener(AuthListener(true, "Fetch books successfully"))
                }
            }
    }

    fun removeCartItem(bookId: String, context: Context) {
        collectionReference = firestore.collection("users").document(firebaseAuth.currentUser!!.uid).collection("cartBook")
        bookDatabase = BookDatabase(context)
        bookDatabase.deleteCart(bookId)
        collectionReference.document(bookId).delete().addOnCompleteListener {
            if (it.isSuccessful) {
                Log.d("NoteService", "delete book id is $bookId")
            } else {
                Log.d(ContentValues.TAG, "get failed with ", it.exception)
            }
        }
    }

    fun removeWishlistItem(bookId: String, context: Context) {
        collectionReference = firestore.collection("users").document(firebaseAuth.currentUser!!.uid).collection("wishList")
        bookDatabase = BookDatabase(context)
        bookDatabase.deleteWish(bookId)
        collectionReference.document(bookId).delete().addOnCompleteListener {
            if (it.isSuccessful) {
                Log.d("NoteService", "delete book id is $bookId")
            } else {
                Log.d(ContentValues.TAG, "get failed with ", it.exception)
            }
        }
    }

}