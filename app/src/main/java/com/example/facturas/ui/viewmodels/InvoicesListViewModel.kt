package com.example.facturas.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.example.facturas.data.appRepository.InvoicesRepository
import com.example.facturas.data.appRepository.models.InvoiceVO

class InvoicesListViewModel : ViewModel() {
    private var _invoices: List<InvoiceVO> = emptyList()
    private val repository = InvoicesRepository.getInstance()

    fun getAllInvoices(): List<InvoiceVO> {
        _invoices = repository.getAllInvoices()
        return _invoices
    }
}