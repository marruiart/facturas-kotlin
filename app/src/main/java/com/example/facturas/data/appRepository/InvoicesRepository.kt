package com.example.facturas.data.appRepository

import android.content.res.AssetManager
import com.example.facturas.data.appRepository.models.InvoiceVO
import com.example.facturas.data.network.NetworkRepository
import com.example.facturas.utils.AppEnvironment

class InvoicesRepository private constructor(
    private val networkRepository: NetworkRepository
) {

    companion object {
        private var _INSTANCE: InvoicesRepository? = null

        fun getInstance(
            assetManager: AssetManager, environment: String = AppEnvironment.PROD_ENVIRONMENT
        ): InvoicesRepository {
            return _INSTANCE ?: InvoicesRepository(
                NetworkRepository.getInstance(assetManager, environment)
            )
        }
    }

    suspend fun getAllInvoices(): List<InvoiceVO> {
        return networkRepository.getAllInvoices().map { it.asInvoiceVO() }
    }
}