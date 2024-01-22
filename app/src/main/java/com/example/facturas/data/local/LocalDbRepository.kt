package com.example.facturas.data.local

import androidx.annotation.WorkerThread
import com.example.facturas.data.local.models.InvoiceEntity
import kotlinx.coroutines.flow.Flow

class LocalDbRepository(private val invoiceDao: InvoiceDao) {

    val getAllInvoices: Flow<List<InvoiceEntity>> = invoiceDao.getAllInvoices()

    @WorkerThread
    suspend fun insertInvoice(invoice: InvoiceEntity) {
        invoiceDao.saveInvoices(listOf(invoice))
    }

    @WorkerThread
    suspend fun insertInvoices(invoices: List<InvoiceEntity>) {
        invoiceDao.saveInvoices(invoices)
    }

    @WorkerThread
    suspend fun deleteAllInvoices() {
        invoiceDao.deleteAll()
    }
}