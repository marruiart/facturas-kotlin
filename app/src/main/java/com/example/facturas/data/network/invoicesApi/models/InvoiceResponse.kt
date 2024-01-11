package com.example.facturas.data.network.invoicesApi.models

data class InvoiceResponse(
    val descEstado: String,
    val fecha: String,
    val importeOrdenacion: Double
) {
    fun asApiModel(): InvoiceApiModel {
        return InvoiceApiModel(
            descEstado,
            fecha,
            importeOrdenacion
        )
    }
}