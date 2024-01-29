package com.example.facturas.data.network

import android.util.Log
import com.example.facturas.data.network.invoicesApi.InvoicesApiService
import com.example.facturas.data.network.invoicesApi.models.InvoiceApiModel
import com.example.facturas.utils.ENVIRONMENT

class NetworkRepository private constructor(
    private val service: InvoicesApiService
) {

    companion object {
        private var _INSTANCE: NetworkRepository? = null

        fun getInstance(): NetworkRepository {
            return _INSTANCE ?: NetworkRepository(InvoicesApiService.getInstance())
        }
    }

    suspend fun getAllInvoices(environment: String = ENVIRONMENT): List<InvoiceApiModel> {
        return try {
            val response = service.getAllInvoices(environment)
            if (response.isSuccessful && response.body() != null) {
                Log.d("DEBUG INVOICES RESPONSE", response.body().toString())
                response.body()!!.getInvoicesList().map { it.asApiModel() }
            } else {
                Log.e("ERROR", response.message())
                emptyList()
            }
        } catch (e: Exception) {
            Log.e("ERROR", e.message.toString())
            emptyList()
        }
    }
}