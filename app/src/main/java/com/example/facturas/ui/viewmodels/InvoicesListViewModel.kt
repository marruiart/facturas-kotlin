package com.example.facturas.ui.viewmodels

import android.accounts.NetworkErrorException
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.facturas.data.appRepository.InvoicesRepository
import com.example.facturas.data.appRepository.models.InvoiceVO
import com.example.facturas.utils.AppEnvironment
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException

class InvoicesListViewModel(
    private val application: Application
) : AndroidViewModel(application) {

    private lateinit var repository: InvoicesRepository
    private var _invoices: MutableStateFlow<List<InvoiceVO>> = MutableStateFlow(emptyList())
    val invoices: StateFlow<List<InvoiceVO>>
        get() = _invoices.asStateFlow()

    fun setRepository(fetchMockData: Boolean = true) {
        repository = InvoicesRepository.getInstance(
            application.applicationContext.assets, getAppEnvironment(fetchMockData)
        )
        viewModelScope.launch {
            try {
                // await for refreshDestinationsList()
                repository.refreshInvoicesList()
            } catch (e: IOException) {
                Log.ERROR
            } catch (e: NetworkErrorException) {
                Log.ERROR
            }
            repository.invoices.collect { invoices ->
                _invoices.value = invoices
            }
        }
    }

    private fun getAppEnvironment(fetchMockData: Boolean): String {
        return if (fetchMockData) AppEnvironment.MOCK_ENVIRONMENT else AppEnvironment.PROD_ENVIRONMENT
    }
}