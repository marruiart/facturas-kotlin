package com.example.facturas.ui.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.facturas.data.appRepository.InvoicesRepository
import com.example.facturas.data.appRepository.models.InvoiceVO
import com.example.facturas.utils.AppEnvironment
import kotlinx.coroutines.launch

class InvoicesListViewModel(
    private val application: Application
) : AndroidViewModel(application) {
    private var _invoices: List<InvoiceVO> = emptyList()
    private lateinit var repository: InvoicesRepository

    fun getAllInvoices(): List<InvoiceVO> {
        viewModelScope.launch {
            _invoices = repository.getAllInvoices()
            Log.d("INVOICES RESPONSE", _invoices.toString())
        }
        return _invoices
    }

    fun setRepository(fetchMockData: Boolean = true) {
        repository = InvoicesRepository.getInstance(
                application.applicationContext.assets, getAppEnvironment(fetchMockData)
            )
    }

    private fun getAppEnvironment(fetchMockData: Boolean): String {
        return if (fetchMockData) AppEnvironment.MOCK_ENVIRONMENT else AppEnvironment.PROD_ENVIRONMENT
    }
}