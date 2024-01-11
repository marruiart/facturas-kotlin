package com.example.facturas.data.appRepository

import com.example.facturas.data.appRepository.models.InvoiceVO
import com.example.facturas.data.network.NetworkRepository

class InvoicesRepository private constructor(
    private val networkRepository: NetworkRepository
) {

    companion object {
        private var _INSTANCE: InvoicesRepository? = null
        fun getInstance(): InvoicesRepository {
            return _INSTANCE ?: InvoicesRepository(NetworkRepository.getInstance())
        }
    }

    fun getAllInvoices(): List<InvoiceVO> {
        return networkRepository.getAllMockInvoices().map { invoice -> invoice.asInvoiceVO() }
    }
}