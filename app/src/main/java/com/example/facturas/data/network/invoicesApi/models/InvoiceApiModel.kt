package com.example.facturas.data.network.invoicesApi.models

import com.example.facturas.data.local.models.InvoiceEntity
import java.time.LocalDate

data class InvoiceApiModel(
    val stateResource: Int,
    val date: LocalDate,
    val amount: Float
) {
    fun asInvoiceEntity(): InvoiceEntity {
        return InvoiceEntity(
            stateResource,
            date,
            amount
        )
    }
}