package com.example.facturas.data.appRepository

import android.content.res.AssetManager
import com.example.facturas.data.appRepository.models.InvoiceVO
import com.example.facturas.data.network.NetworkRepository
import com.example.facturas.utils.ENVIRONMENT
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.withContext

class InvoicesRepository private constructor(
    private val networkRepository: NetworkRepository
) {
    var _invoices: List<InvoiceVO> = emptyList()
    val invoices: Flow<List<InvoiceVO>>
        get() = flowOf(_invoices)

    companion object {
        private var _INSTANCE: InvoicesRepository? = null

        fun getInstance(
            assetManager: AssetManager, environment: String = ENVIRONMENT
        ): InvoicesRepository {
            return _INSTANCE ?: InvoicesRepository(
                NetworkRepository.getInstance(assetManager, environment)
            )
        }
    }

    suspend fun refreshInvoicesList(): List<InvoiceVO> = withContext(Dispatchers.IO) {
        // SCOPE: suspendable code -> executed asynchronously in a coroutine.
        // Dispatchers.IO is a special thread for network operations
        val invoicesList = networkRepository.getAllInvoices().map { it.asInvoiceVO() }
        _invoices = invoicesList
        invoicesList
    }
}