package com.example.facturas.ui.viewmodels

import android.accounts.NetworkErrorException
import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.facturas.data.appRepository.InvoicesRepository
import com.example.facturas.data.appRepository.models.InvoiceVO
import com.example.facturas.services.FilterService
import com.example.facturas.utils.Dates
import com.example.facturas.utils.ENVIRONMENT
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException

class InvoicesListViewModel : ViewModel() {

    private lateinit var repository: InvoicesRepository
    var filter = FilterService.filter
    private var _invoices: MutableStateFlow<List<InvoiceVO>> = MutableStateFlow(emptyList())
    private var _filteredInvoices: MutableStateFlow<List<InvoiceVO>> = MutableStateFlow(emptyList())
    val invoices: StateFlow<List<InvoiceVO>>
        get() = _filteredInvoices.asStateFlow()
    lateinit var appContext: Context

    fun initRepository() {
        setRepository(ENVIRONMENT)
    }

    fun setRepository(environment: String) {
        repository = getRepositoryInstance(environment)
        populateListData()
    }

    private fun getRepositoryInstance(environment: String): InvoicesRepository {
        return InvoicesRepository.getInstance(appContext.applicationContext, environment)
    }

    private fun populateListData() {
        repository.invoices
        viewModelScope.launch {
            refreshRepositoryData()
            repository.invoices.collect { invoices ->
                _invoices.value = invoices
                _filteredInvoices.value = invoices
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

    fun applyFilter() {
        var filteredInvoices: MutableList<InvoiceVO> = _invoices.value.toMutableList()
        filteredInvoices.filter { invoice ->
            checkFilterByDate(invoice) && checkFilterByAmount(invoice) && checkFilterByState(invoice)
        }
        _filteredInvoices.value = filteredInvoices
    }

    private fun checkFilterByDate(invoice: InvoiceVO): Boolean {
        return invoice.date in (filter.dates.from ?: Dates.EPOCH_DATE)..(filter.dates.to
            ?: Dates.NOW)
    }

    private fun checkFilterByAmount(invoice: InvoiceVO): Boolean {
        return invoice.amount in (filter.selectedAmount.min
            ?: filter.amountRange.min)..(filter.selectedAmount.max ?: filter.amountRange.max)
    }

    private fun checkFilterByState(invoice: InvoiceVO): Boolean {
        return if (filter.state.values.contains(true)) {
            filter.state.getOrDefault(invoice.stateResource, true)
        } else {
            true
        }
    }
}