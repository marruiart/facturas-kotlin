package com.example.facturas.data.network

import com.example.facturas.data.network.invoicesApi.InvoicesApiService
import com.example.facturas.data.network.invoicesApi.models.InvoiceApiModel

class NetworkRepository private constructor(
    private val service: InvoicesApiService
) {

    companion object {
        private var _INSTANCE: NetworkRepository? = null
        fun getInstance(): NetworkRepository {
            return _INSTANCE ?: NetworkRepository(InvoicesApiService())
        }
    }

    fun getAllMockInvoices(): List<InvoiceApiModel> {
        return service.getAllMockInvoices().getInvoicesList()
            .map { invoice -> invoice.asApiModel() }
    }
}