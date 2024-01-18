package com.example.facturas.data.network

import android.content.res.AssetManager
import android.util.Log
import com.example.facturas.data.network.invoicesApi.InvoicesApiService
import com.example.facturas.data.network.invoicesApi.models.InvoiceApiModel
import com.example.facturas.utils.AppEnvironment
import com.example.facturas.utils.ENVIRONMENT

class NetworkRepository private constructor(
    private val service: InvoicesApiService
) {

    companion object {
        private var _MOCK_INSTANCE: NetworkRepository? = null
        private var _INSTANCE: NetworkRepository? = null

        fun getInstance(
            assetManager: AssetManager, environment: String = ENVIRONMENT
        ): NetworkRepository {
            return if (environment == AppEnvironment.MOCK_ENVIRONMENT) {
                _MOCK_INSTANCE ?: NetworkRepository(InvoicesApiService(assetManager, environment))
            } else {
                _INSTANCE ?: NetworkRepository(InvoicesApiService(assetManager, environment))
            }
        }
    }

    suspend fun getAllInvoices(): List<InvoiceApiModel> {
        return try {
            val response = service.api.getAllInvoices()
            if (response.isSuccessful && response.body() != null) {
                Log.d("INVOICES RESPONSE", response.body().toString())
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