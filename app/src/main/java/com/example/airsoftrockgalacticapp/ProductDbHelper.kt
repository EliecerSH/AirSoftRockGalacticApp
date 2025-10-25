package com.example.airsoftrockgalacticapp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns

object ProductContract {
    object ProductEntry : BaseColumns {
        const val TABLE_NAME = "products"
        const val COLUMN_NAME_SLUG = "slug"
        const val COLUMN_NAME_NOMBRE = "nombre"
        const val COLUMN_NAME_PRECIO = "precio"
        const val COLUMN_NAME_IMG = "img"
        const val COLUMN_NAME_CANTIDAD = "cantidad"
        const val COLUMN_NAME_TIPO = "tipo"
        const val COLUMN_NAME_DESC = "desc"
    }
}

private const val SQL_CREATE_ENTRIES = """
    CREATE TABLE ${ProductContract.ProductEntry.TABLE_NAME} (
        ${BaseColumns._ID} INTEGER PRIMARY KEY,
        ${ProductContract.ProductEntry.COLUMN_NAME_SLUG} TEXT,
        ${ProductContract.ProductEntry.COLUMN_NAME_NOMBRE} TEXT,
        ${ProductContract.ProductEntry.COLUMN_NAME_PRECIO} REAL,
        ${ProductContract.ProductEntry.COLUMN_NAME_IMG} TEXT,
        ${ProductContract.ProductEntry.COLUMN_NAME_CANTIDAD} INTEGER,
        ${ProductContract.ProductEntry.COLUMN_NAME_TIPO} TEXT,
        ${ProductContract.ProductEntry.COLUMN_NAME_DESC} TEXT
    )
"""

private const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${ProductContract.ProductEntry.TABLE_NAME}"

class ProductDbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ENTRIES)
        insertInitialData(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }

    private fun insertInitialData(db: SQLiteDatabase) {
        val products = listOf(
            ContentValues().apply {
                put(ProductContract.ProductEntry.COLUMN_NAME_SLUG, "m4a1-carbine")
                put(ProductContract.ProductEntry.COLUMN_NAME_NOMBRE, "M4A1 Carbine")
                put(ProductContract.ProductEntry.COLUMN_NAME_PRECIO, 150000.0)
                put(ProductContract.ProductEntry.COLUMN_NAME_IMG, "arm01")
                put(ProductContract.ProductEntry.COLUMN_NAME_CANTIDAD, 10)
                put(ProductContract.ProductEntry.COLUMN_NAME_TIPO, "rifle")
                put(ProductContract.ProductEntry.COLUMN_NAME_DESC, "Rifle de asalto eléctrico (AEG). Versátil y altamente personalizable.")
            },
            ContentValues().apply {
                put(ProductContract.ProductEntry.COLUMN_NAME_SLUG, "ak47-tactical")
                put(ProductContract.ProductEntry.COLUMN_NAME_NOMBRE, "AK-47 Tactical")
                put(ProductContract.ProductEntry.COLUMN_NAME_PRECIO, 170000.0)
                put(ProductContract.ProductEntry.COLUMN_NAME_IMG, "arm02")
                put(ProductContract.ProductEntry.COLUMN_NAME_CANTIDAD, 11)
                put(ProductContract.ProductEntry.COLUMN_NAME_TIPO, "rifle")
                put(ProductContract.ProductEntry.COLUMN_NAME_DESC, "Réplica robusta con alto impacto por disparo.")
            },
            ContentValues().apply {
                put(ProductContract.ProductEntry.COLUMN_NAME_SLUG, "glock-17")
                put(ProductContract.ProductEntry.COLUMN_NAME_NOMBRE, "Glock 17")
                put(ProductContract.ProductEntry.COLUMN_NAME_PRECIO, 60000.0)
                put(ProductContract.ProductEntry.COLUMN_NAME_IMG, "arm03")
                put(ProductContract.ProductEntry.COLUMN_NAME_CANTIDAD, 21)
                put(ProductContract.ProductEntry.COLUMN_NAME_TIPO, "pistola")
                put(ProductContract.ProductEntry.COLUMN_NAME_DESC, "Pistola de gas (GBB) semiautomática; buen realismo de retroceso.")
            },
            ContentValues().apply {
                put(ProductContract.ProductEntry.COLUMN_NAME_SLUG, "desert-eagle")
                put(ProductContract.ProductEntry.COLUMN_NAME_NOMBRE, "Desert Eagle")
                put(ProductContract.ProductEntry.COLUMN_NAME_PRECIO, 80000.0)
                put(ProductContract.ProductEntry.COLUMN_NAME_IMG, "arm04")
                put(ProductContract.ProductEntry.COLUMN_NAME_CANTIDAD, 31)
                put(ProductContract.ProductEntry.COLUMN_NAME_TIPO, "pistola")
                put(ProductContract.ProductEntry.COLUMN_NAME_DESC, "Pistola de gran apariencia y alto daño por disparo.")
            },
            ContentValues().apply {
                put(ProductContract.ProductEntry.COLUMN_NAME_SLUG, "fn-scar-l")
                put(ProductContract.ProductEntry.COLUMN_NAME_NOMBRE, "FN SCAR-L")
                put(ProductContract.ProductEntry.COLUMN_NAME_PRECIO, 180000.0)
                put(ProductContract.ProductEntry.COLUMN_NAME_IMG, "arm05")
                put(ProductContract.ProductEntry.COLUMN_NAME_CANTIDAD, 12)
                put(ProductContract.ProductEntry.COLUMN_NAME_TIPO, "rifle")
                put(ProductContract.ProductEntry.COLUMN_NAME_DESC, "Rifle AEG modular con buena precisión.")
            },
            ContentValues().apply {
                put(ProductContract.ProductEntry.COLUMN_NAME_SLUG, "hk416d")
                put(ProductContract.ProductEntry.COLUMN_NAME_NOMBRE, "HK416D")
                put(ProductContract.ProductEntry.COLUMN_NAME_PRECIO, 150000.0)
                put(ProductContract.ProductEntry.COLUMN_NAME_IMG, "arm06")
                put(ProductContract.ProductEntry.COLUMN_NAME_CANTIDAD, 11)
                put(ProductContract.ProductEntry.COLUMN_NAME_TIPO, "rifle")
                put(ProductContract.ProductEntry.COLUMN_NAME_DESC, "Variante moderna del M4, con ergonomía mejorada.")
            },
            ContentValues().apply {
                put(ProductContract.ProductEntry.COLUMN_NAME_SLUG, "m249-saw")
                put(ProductContract.ProductEntry.COLUMN_NAME_NOMBRE, "M249 SAW")
                put(ProductContract.ProductEntry.COLUMN_NAME_PRECIO, 200000.0)
                put(ProductContract.ProductEntry.COLUMN_NAME_IMG, "arm07")
                put(ProductContract.ProductEntry.COLUMN_NAME_CANTIDAD, 17)
                put(ProductContract.ProductEntry.COLUMN_NAME_TIPO, "lmg")
                put(ProductContract.ProductEntry.COLUMN_NAME_DESC, "Réplica de soporte con gran capacidad de fuego sostenido.")
            },
            ContentValues().apply {
                put(ProductContract.ProductEntry.COLUMN_NAME_SLUG, "mp5")
                put(ProductContract.ProductEntry.COLUMN_NAME_NOMBRE, "MP5 Submachine Gun")
                put(ProductContract.ProductEntry.COLUMN_NAME_PRECIO, 100000.0)
                put(ProductContract.ProductEntry.COLUMN_NAME_IMG, "arm08")
                put(ProductContract.ProductEntry.COLUMN_NAME_CANTIDAD, 16)
                put(ProductContract.ProductEntry.COLUMN_NAME_TIPO, "subfusil")
                put(ProductContract.ProductEntry.COLUMN_NAME_DESC, "Compacto y manejable — excelente en espacios cerrados.")
            },
            ContentValues().apply {
                put(ProductContract.ProductEntry.COLUMN_NAME_SLUG, "p90")
                put(ProductContract.ProductEntry.COLUMN_NAME_NOMBRE, "P90")
                put(ProductContract.ProductEntry.COLUMN_NAME_PRECIO, 130000.0)
                put(ProductContract.ProductEntry.COLUMN_NAME_IMG, "arm09")
                put(ProductContract.ProductEntry.COLUMN_NAME_CANTIDAD, 10)
                put(ProductContract.ProductEntry.COLUMN_NAME_TIPO, "subfusil")
                put(ProductContract.ProductEntry.COLUMN_NAME_DESC, "Diseño compacto con cargador superior de alta capacidad.")
            },
            ContentValues().apply {
                put(ProductContract.ProductEntry.COLUMN_NAME_SLUG, "m870-tactical")
                put(ProductContract.ProductEntry.COLUMN_NAME_NOMBRE, "M870 Tactical")
                put(ProductContract.ProductEntry.COLUMN_NAME_PRECIO, 160000.0)
                put(ProductContract.ProductEntry.COLUMN_NAME_IMG, "arm10")
                put(ProductContract.ProductEntry.COLUMN_NAME_CANTIDAD, 15)
                put(ProductContract.ProductEntry.COLUMN_NAME_TIPO, "escopeta")
                put(ProductContract.ProductEntry.COLUMN_NAME_DESC, "Escopeta táctica de corto alcance, ideal para entradas.")
            },
            ContentValues().apply {
                put(ProductContract.ProductEntry.COLUMN_NAME_SLUG, "l96-aws")
                put(ProductContract.ProductEntry.COLUMN_NAME_NOMBRE, "Rifle L96 AWS")
                put(ProductContract.ProductEntry.COLUMN_NAME_PRECIO, 220000.0)
                put(ProductContract.ProductEntry.COLUMN_NAME_IMG, "arm11")
                put(ProductContract.ProductEntry.COLUMN_NAME_CANTIDAD, 22)
                put(ProductContract.ProductEntry.COLUMN_NAME_TIPO, "sniper")
                put(ProductContract.ProductEntry.COLUMN_NAME_DESC, "Réplica bolt-action de alta precisión para largo alcance.")
            },
            ContentValues().apply {
                put(ProductContract.ProductEntry.COLUMN_NAME_SLUG, "ump45")
                put(ProductContract.ProductEntry.COLUMN_NAME_NOMBRE, "UMP45")
                put(ProductContract.ProductEntry.COLUMN_NAME_PRECIO, 120000.0)
                put(ProductContract.ProductEntry.COLUMN_NAME_IMG, "arm12")
                put(ProductContract.ProductEntry.COLUMN_NAME_CANTIDAD, 20)
                put(ProductContract.ProductEntry.COLUMN_NAME_TIPO, "subfusil")
                put(ProductContract.ProductEntry.COLUMN_NAME_DESC, "Compacto y con buen balance entre daño y control.")
            }
        )

        products.forEach { 
            db.insert(ProductContract.ProductEntry.TABLE_NAME, null, it) 
        }
    }

    companion object {
        const val DATABASE_VERSION = 2 // Incremented version to trigger onUpgrade
        const val DATABASE_NAME = "Products.db"
    }
}