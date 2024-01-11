package com.example.facturas.data.network.invoicesApi.models

import com.example.facturas.data.appRepository.models.InvoiceVO

data class InvoiceApiModel(
    val state: String,
    val date: String,
    val amount: Double
) {
    fun asInvoiceVO(): InvoiceVO {
        return InvoiceVO(
            state,
            date,
            amount
        )
    }
}