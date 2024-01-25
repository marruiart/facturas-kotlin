package com.example.facturas.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.facturas.data.local.converters.DateConverter
import com.example.facturas.data.local.models.InvoiceEntity

@Database(entities = [InvoiceEntity::class], version = 1)
@TypeConverters(DateConverter::class)
abstract class InvoicesDatabase : RoomDatabase() {

    companion object {
        @Volatile // Avoid concurrency issues
        private var _INSTANCE: InvoicesDatabase? = null

        fun getInstance(context: Context): InvoicesDatabase {
            return _INSTANCE ?: synchronized(this) {
                _INSTANCE ?: buildDatabase(context).also { db -> _INSTANCE = db }
            }
        }

        private fun buildDatabase(context: Context): InvoicesDatabase {
            return Room.databaseBuilder(
                context.applicationContext, // context
                InvoicesDatabase::class.java, // db
                "invoices_db" // db name
            ).build()
        }
    }

    abstract fun invoicesDao(): InvoiceDao
}