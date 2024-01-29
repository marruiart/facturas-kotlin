package com.example.facturas.data.appRepository

import com.example.facturas.data.appRepository.models.InvoiceVO
import com.example.facturas.data.local.InvoicesDatabase
import com.example.facturas.data.local.LocalDbRepository
import com.example.facturas.data.local.models.InvoiceEntity
import com.example.facturas.data.local.models.asInvoiceVOList
import com.example.facturas.data.network.NetworkRepository
import com.example.facturas.utils.ENVIRONMENT
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class InvoicesRepository private constructor(
    private val networkRepository: NetworkRepository,
    private val localDbRepository: LocalDbRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    val invoices: Flow<List<InvoiceVO>>
        get() = localDbRepository.getAllInvoices.map { it.asInvoiceVOList() }
    private var _environment: String = ENVIRONMENT

    companion object {
        private var _INSTANCE: InvoicesRepository? = null

        fun getInstance(): InvoicesRepository {
            return _INSTANCE ?: InvoicesRepository(
                NetworkRepository.getInstance(),
                LocalDbRepository(InvoicesDatabase.getInstance().invoicesDao())
            )
        }
    }

    fun setEnvironment(environment: String) {
        _environment = environment
    }

    suspend fun refreshInvoicesList() = withContext(dispatcher) {
        // SCOPE: suspendable code -> executed asynchronously in a coroutine.
        // Dispatchers.IO is a special thread for network operations
        val invoicesList = networkRepository.getAllInvoices(_environment)
        refreshLocalDb(invoicesList.map { it.asInvoiceEntity() })
    }

    private suspend fun refreshLocalDb(invoices: List<InvoiceEntity>) {
        localDbRepository.deleteAllInvoices()
        saveInvoicesToLocalDb(invoices)
    }

    private suspend fun saveInvoicesToLocalDb(invoices: List<InvoiceEntity>) =
        localDbRepository.insertInvoices(invoices)
}