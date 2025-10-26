package com.example.airsoftrockgalacticapp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns

object PaymentContract {
    object PaymentEntry : BaseColumns {
        const val TABLE_NAME = "payments"
        // Datos Personales
        const val COLUMN_NAME_NAME = "name"
        const val COLUMN_NAME_RUT = "rut"
        const val COLUMN_NAME_PHONE = "phone"
        const val COLUMN_NAME_USER_EMAIL = "user_email"
        // Dirección
        const val COLUMN_NAME_REGION = "region"
        const val COLUMN_NAME_COMMUNE = "commune"
        const val COLUMN_NAME_ADDRESS = "address"
        // Método de Pago
        const val COLUMN_NAME_CARD_NUMBER = "card_number"
        const val COLUMN_NAME_CARD_HOLDER_NAME = "card_holder_name"
        const val COLUMN_NAME_EXPIRY_DATE = "expiry_date"
        const val COLUMN_NAME_CVV = "cvv"
        // Monto y Fecha
        const val COLUMN_NAME_TOTAL_AMOUNT = "total_amount"
        const val COLUMN_NAME_PURCHASE_DATE = "purchase_date"
    }
}

private const val SQL_CREATE_ENTRIES = """
    CREATE TABLE ${PaymentContract.PaymentEntry.TABLE_NAME} (
        ${BaseColumns._ID} INTEGER PRIMARY KEY,
        ${PaymentContract.PaymentEntry.COLUMN_NAME_NAME} TEXT,
        ${PaymentContract.PaymentEntry.COLUMN_NAME_RUT} TEXT,
        ${PaymentContract.PaymentEntry.COLUMN_NAME_PHONE} TEXT,
        ${PaymentContract.PaymentEntry.COLUMN_NAME_USER_EMAIL} TEXT,
        ${PaymentContract.PaymentEntry.COLUMN_NAME_REGION} TEXT,
        ${PaymentContract.PaymentEntry.COLUMN_NAME_COMMUNE} TEXT,
        ${PaymentContract.PaymentEntry.COLUMN_NAME_ADDRESS} TEXT,
        ${PaymentContract.PaymentEntry.COLUMN_NAME_CARD_NUMBER} TEXT,
        ${PaymentContract.PaymentEntry.COLUMN_NAME_CARD_HOLDER_NAME} TEXT,
        ${PaymentContract.PaymentEntry.COLUMN_NAME_EXPIRY_DATE} TEXT,
        ${PaymentContract.PaymentEntry.COLUMN_NAME_CVV} TEXT,
        ${PaymentContract.PaymentEntry.COLUMN_NAME_TOTAL_AMOUNT} REAL,
        ${PaymentContract.PaymentEntry.COLUMN_NAME_PURCHASE_DATE} TEXT
    )
"""

private const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${PaymentContract.PaymentEntry.TABLE_NAME}"

class PaymentDbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }

    fun addPayment(
        name: String, rut: String, phone: String, userEmail: String,
        region: String, commune: String, address: String,
        cardNumber: String, cardHolderName: String, expiryDate: String, cvv: String,
        totalAmount: Double, purchaseDate: String
    ) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(PaymentContract.PaymentEntry.COLUMN_NAME_NAME, name)
            put(PaymentContract.PaymentEntry.COLUMN_NAME_RUT, rut)
            put(PaymentContract.PaymentEntry.COLUMN_NAME_PHONE, phone)
            put(PaymentContract.PaymentEntry.COLUMN_NAME_USER_EMAIL, userEmail)
            put(PaymentContract.PaymentEntry.COLUMN_NAME_REGION, region)
            put(PaymentContract.PaymentEntry.COLUMN_NAME_COMMUNE, commune)
            put(PaymentContract.PaymentEntry.COLUMN_NAME_ADDRESS, address)
            put(PaymentContract.PaymentEntry.COLUMN_NAME_CARD_NUMBER, cardNumber)
            put(PaymentContract.PaymentEntry.COLUMN_NAME_CARD_HOLDER_NAME, cardHolderName)
            put(PaymentContract.PaymentEntry.COLUMN_NAME_EXPIRY_DATE, expiryDate)
            put(PaymentContract.PaymentEntry.COLUMN_NAME_CVV, cvv)
            put(PaymentContract.PaymentEntry.COLUMN_NAME_TOTAL_AMOUNT, totalAmount)
            put(PaymentContract.PaymentEntry.COLUMN_NAME_PURCHASE_DATE, purchaseDate)
        }
        db.insert(PaymentContract.PaymentEntry.TABLE_NAME, null, values)
    }

    companion object {
        const val DATABASE_VERSION = 5 // Incremented version to trigger onUpgrade
        const val DATABASE_NAME = "Payment.db"
    }
}