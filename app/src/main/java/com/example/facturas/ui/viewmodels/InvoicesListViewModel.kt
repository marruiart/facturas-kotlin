package com.example.facturas.ui.viewmodels

import android.accounts.NetworkErrorException
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.facturas.data.appRepository.InvoicesRepository
import com.example.facturas.data.appRepository.models.InvoiceVO
import com.example.facturas.utils.ENVIRONMENT
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

    fun initRepository() {
        setRepository(ENVIRONMENT)
    }

    fun setRepository(environment: String) {
        repository = getRepositoryInstance(environment)
        populateListData()
    }

    private fun getRepositoryInstance(environment: String): InvoicesRepository {
        return InvoicesRepository.getInstance(application.applicationContext, environment)
    }

    private fun populateListData() {
        repository.invoices
        viewModelScope.launch {
            refreshRepositoryData()
            repository.invoices.collect { invoices ->
                _invoices.value = invoices
            }
        }
    }

    private suspend fun refreshRepositoryData() {
        try {
            // await for refreshing
            repository.refreshInvoicesList()
        } catch (e: IOException) {
            Log.ERROR
        } catch (e: NetworkErrorException) {
            Log.ERROR
        }
    }
}