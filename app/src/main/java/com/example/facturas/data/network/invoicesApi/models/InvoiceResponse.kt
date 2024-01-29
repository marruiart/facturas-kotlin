package com.example.facturas.data.network.invoicesApi.models

import com.example.facturas.R
import com.example.facturas.utils.Dates
import java.time.LocalDate

data class InvoiceResponse(
    val descEstado: String,
    val fecha: String,
    val importeOrdenacion: Float
) {
    fun asApiModel(): InvoiceApiModel {
        return InvoiceApiModel(
            getTranslatedState(descEstado),
            LocalDate.parse(fecha, Dates.FORMATTER),
            importeOrdenacion
        )
    }

    private fun getTranslatedState(state: String): Int {
        return when (state) {
            "Pagada" -> R.string.invoice_item_paid
            "Anulada" -> R.string.invoice_item_cancelled
            "Cuota fija" -> R.string.invoice_item_fixed_fee
            "Pendiente de pago" -> R.string.invoice_item_pending
            "Plan de pago" -> R.string.invoice_item_payment_plan
            else -> R.string.invoice_item_state_empty
        }
    }
}