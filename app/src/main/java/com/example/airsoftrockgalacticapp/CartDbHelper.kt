package com.example.airsoftrockgalacticapp

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import android.annotation.SuppressLint

object CartContract {
    object CartEntry : BaseColumns {
        const val TABLE_NAME = "cart"
        const val COLUMN_NAME_PRODUCT_ID = "product_id"
        const val COLUMN_NAME_QUANTITY = "quantity"
    }
}

private const val SQL_CREATE_ENTRIES = """
    CREATE TABLE ${CartContract.CartEntry.TABLE_NAME} (
        ${BaseColumns._ID} INTEGER PRIMARY KEY,
        ${CartContract.CartEntry.COLUMN_NAME_PRODUCT_ID} INTEGER UNIQUE,
        ${CartContract.CartEntry.COLUMN_NAME_QUANTITY} INTEGER
    )
"""

private const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${CartContract.CartEntry.TABLE_NAME}"

class CartDbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }

    @SuppressLint("Range")
    fun addProductToCart(productId: Long) {
        val db = writableDatabase
        
        val selectQuery = "SELECT * FROM ${CartContract.CartEntry.TABLE_NAME} WHERE ${CartContract.CartEntry.COLUMN_NAME_PRODUCT_ID} = ?"
        val cursor = db.rawQuery(selectQuery, arrayOf(productId.toString()))

        if (cursor.moveToFirst()) {
            val currentQuantity = cursor.getInt(cursor.getColumnIndex(CartContract.CartEntry.COLUMN_NAME_QUANTITY))
            val newQuantity = currentQuantity + 1
            
            val values = ContentValues().apply {
                put(CartContract.CartEntry.COLUMN_NAME_QUANTITY, newQuantity)
            }
            db.update(CartContract.CartEntry.TABLE_NAME, values, "${CartContract.CartEntry.COLUMN_NAME_PRODUCT_ID} = ?", arrayOf(productId.toString()))

        } else {
            val values = ContentValues().apply {
                put(CartContract.CartEntry.COLUMN_NAME_PRODUCT_ID, productId)
                put(CartContract.CartEntry.COLUMN_NAME_QUANTITY, 1)
            }
            db.insert(CartContract.CartEntry.TABLE_NAME, null, values)
        }
        cursor.close()
    }

    fun getCartItems(): Cursor {
        val db = readableDatabase
        return db.query(CartContract.CartEntry.TABLE_NAME, null, null, null, null, null, null)
    }

    fun clearCart() {
        val db = writableDatabase
        db.delete(CartContract.CartEntry.TABLE_NAME, null, null)
    }

    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "Cart.db"
    }
}