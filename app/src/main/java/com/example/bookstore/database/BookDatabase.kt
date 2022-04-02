package com.example.bookstore.database

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.bookstore.model.Book

class BookDatabase(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    private val db: SQLiteDatabase = this.writableDatabase
    override fun onCreate(db: SQLiteDatabase) {
         val queryBook = ("CREATE TABLE $BOOK_TABLE (" +
                "$BOOK_NAME TEXT," +
                "$BOOK_AUTHOR TEXT," +
                "$BOOK_PRICE TEXT," +
                "$BOOK_IMG_URL TEXT," +
                "$FIRE_STORE_BOOK_ID TEXT)")
         val cartBook = ("CREATE TABLE $CART_TABLE (" +
                "$BOOK_NAME TEXT," +
                "$BOOK_AUTHOR TEXT," +
                "$BOOK_PRICE TEXT," +
                "$BOOK_IMG_URL TEXT," +
                "$FIRE_STORE_BOOK_ID TEXT," +
                "$FIRE_STORE_CUSTOMER_ID TEXT)")
         val wishlistBook = ("CREATE TABLE $WISHLIST_TABLE (" +
                "$BOOK_NAME TEXT," +
                "$BOOK_AUTHOR TEXT," +
                "$BOOK_PRICE TEXT," +
                "$BOOK_IMG_URL TEXT," +
                "$FIRE_STORE_BOOK_ID TEXT," +
                "$FIRE_STORE_CUSTOMER_ID TEXT)")
        db.execSQL(queryBook)
        db.execSQL(cartBook)
        db.execSQL(wishlistBook)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $BOOK_TABLE")
        db.execSQL("DROP TABLE IF EXISTS $CART_TABLE")
        db.execSQL("DROP TABLE IF EXISTS $WISHLIST_TABLE")
        onCreate(db)
    }

    fun saveBook(book: Book) {
        val values = ContentValues()
        values.put(BOOK_NAME, book.book_Name)
        values.put(BOOK_AUTHOR, book.book_Author)
        values.put(BOOK_PRICE, book.book_Price)
        values.put(BOOK_IMG_URL, book.book_imgUrl)
        values.put(FIRE_STORE_BOOK_ID, book.book_id)
        db.insert(BOOK_TABLE, null, values)
//        db.close()
    }

    fun saveWish(book: Book) {
        val values = ContentValues()
        values.put(BOOK_NAME, book.book_Name)
        values.put(BOOK_AUTHOR, book.book_Author)
        values.put(BOOK_PRICE, book.book_Price)
        values.put(BOOK_IMG_URL, book.book_imgUrl)
        values.put(FIRE_STORE_BOOK_ID, book.book_id)
        values.put(FIRE_STORE_CUSTOMER_ID, book.user_id)
        db.insert(WISHLIST_TABLE, null, values)
//      db.close()
    }

    fun saveCart(book: Book) {
        val values = ContentValues()
        values.put(BOOK_NAME, book.book_Name)
        values.put(BOOK_AUTHOR, book.book_Author)
        values.put(BOOK_PRICE, book.book_Price)
        values.put(BOOK_IMG_URL, book.book_imgUrl)
        values.put(FIRE_STORE_BOOK_ID, book.book_id)
        values.put(FIRE_STORE_CUSTOMER_ID, book.user_id)
        db.insert(CART_TABLE, null, values)
//      db.close()
    }

    @SuppressLint("Recycle")
    fun getBook(): Cursor? {
        Log.d("BookDatabase", "data is ${db.rawQuery("SELECT * FROM $BOOK_TABLE", null)}")
        return db.rawQuery("SELECT * FROM $BOOK_TABLE", null)
    }

    @SuppressLint("Recycle")
    fun getCart(): Cursor? {
        Log.d("BookDatabase", "data is ${db.rawQuery("SELECT * FROM $BOOK_TABLE", null)}")
        return db.rawQuery("SELECT * FROM $CART_TABLE", null)
    }

    @SuppressLint("Recycle")
    fun getWish(): Cursor? {
        Log.d("BookDatabase", "data is ${db.rawQuery("SELECT * FROM $BOOK_TABLE", null)}")
        return db.rawQuery("SELECT * FROM $WISHLIST_TABLE", null)
    }

    @SuppressLint("Recycle")
    fun bookCount(): Int {
        var count = 0
        val sql = "SELECT COUNT(*) FROM $BOOK_TABLE"
        val cursor =  db.rawQuery(sql, null)
        if (cursor.count > 0) {
            cursor.moveToFirst()
            count = cursor.getInt(0)
        }
        cursor.close()
        return count
    }

    fun deleteCart(id: String) {
        db.delete(CART_TABLE, "$FIRE_STORE_BOOK_ID = ?", arrayOf(id))
    }

    fun deleteWish(id: String) {
        db.delete(WISHLIST_TABLE, "$FIRE_STORE_BOOK_ID = ?", arrayOf(id))
    }

    @SuppressLint("Recycle")
    fun existItem(id: String): Boolean {
        val cursor = db.rawQuery("Select * from $BOOK_TABLE where $FIRE_STORE_BOOK_ID = '$id'", null )
        return if (cursor.moveToFirst()) {
            db.close()
            true
        } else {
            false
        }
    }

    companion object {
        const val DATABASE_NAME = "BookDataSql"
        const val DATABASE_VERSION = 1
        const val BOOK_TABLE = "Book"
        const val CART_TABLE = "cartBook"
        const val WISHLIST_TABLE = "wishBook"
        const val FIRE_STORE_CUSTOMER_ID = "customer_id"
        const val FIRE_STORE_BOOK_ID = "book_id"
        const val BOOK_NAME = "bookName"
        const val BOOK_AUTHOR = "bookAuthor"
        const val BOOK_PRICE = "bookPrice"
        const val BOOK_IMG_URL = "bookImgUrl"
    }

}