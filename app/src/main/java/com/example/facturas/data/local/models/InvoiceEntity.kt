package com.example.facturas.data.local.models

import androidx.room.Entity
import com.example.facturas.data.appRepository.models.InvoiceVO

@Entity(tableName = "invoice", primaryKeys = ["date", "amount"])
data class InvoiceEntity(
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

fun List<InvoiceEntity>.asInvoiceVOList(): List<InvoiceVO> {
    return this.map {
        it.asInvoiceVO()
    }
}
