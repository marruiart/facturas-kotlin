package com.example.facturas.data.network.invoicesApi.models

import com.example.facturas.data.appRepository.models.InvoiceVO
import com.example.facturas.data.local.models.InvoiceEntity

data class InvoiceApiModel(
    val state: String,
    val date: String,
    val amount: Double
) {
    fun asInvoiceEntity(): InvoiceEntity {
        return InvoiceEntity(
            state,
            date,
            amount
        )
    }
}