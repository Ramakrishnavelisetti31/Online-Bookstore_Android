package com.example.bookstore.database

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.bookstore.model.Customer

class CustomerDatabase(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    private var db: SQLiteDatabase = this.writableDatabase
    override fun onCreate(db: SQLiteDatabase?) {
        val query = ("CREATE TABLE $TABLE_NAME (" +
                "$CUSTOMER_NAME TEXT," +
                "$CUSTOMER_EMAIL TEXT," +
                "$CUSTOMER_PASSWORD TEXT," +
                "$CUSTOMER_MOBILE TEXT," +
                "$FIRE_STORE_CUSTOMER_ID TEXT)")
                db?.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun saveData(customer: Customer) {
        val values = ContentValues()
        values.put(CUSTOMER_NAME, customer.userName)
        values.put(CUSTOMER_EMAIL, customer.email)
        values.put(CUSTOMER_PASSWORD, customer.password)
        values.put(CUSTOMER_MOBILE, customer.mobileNumber)
        values.put(FIRE_STORE_CUSTOMER_ID, customer.customer_id)
        db.insert(TABLE_NAME, null, values)
//      db.close()
    }

    @SuppressLint("Recycle")
    fun getData(): Cursor? {
        Log.d("CustomerDatabase", "data is ${db.rawQuery("SELECT * FROM $TABLE_NAME", null)}")
        return db.rawQuery("SELECT * FROM $TABLE_NAME", null)
    }

    @SuppressLint("Recycle")
    fun loginCheck(email: String, password: String): Boolean {
        val cursor = db.query(TABLE_NAME, null, CUSTOMER_EMAIL, arrayOf(email), null, null, null)
        return if (cursor.count < 1) {
            cursor.close()
            false
        } else {
            true
        }
    }

    fun upDateData(customer: Customer) {
        val values = ContentValues()
        values.put(CUSTOMER_NAME, customer.userName)
        values.put(CUSTOMER_EMAIL, customer.email)
        values.put(CUSTOMER_PASSWORD, customer.password)
        values.put(CUSTOMER_MOBILE, customer.mobileNumber)
        values.put(FIRE_STORE_CUSTOMER_ID, customer.customer_id)
        db.update(TABLE_NAME, values, "$FIRE_STORE_CUSTOMER_ID = ?", arrayOf(customer.customer_id))
    }

    fun deleteData(id: String) {
        db.delete(TABLE_NAME, "${FIRE_STORE_CUSTOMER_ID}_ID = ?", arrayOf(id))
    }

    companion object {
        const val DATABASE_NAME = "CustomerDataSql"
        const val DATABASE_VERSION = 1
        const val TABLE_NAME = "Customer"
        const val FIRE_STORE_CUSTOMER_ID = "customer_id"
        const val CUSTOMER_NAME = "customerName"
        const val CUSTOMER_EMAIL = "customerEmail"
        const val CUSTOMER_PASSWORD = "customerPassword"
        const val CUSTOMER_MOBILE = "customerMobile"
    }

}