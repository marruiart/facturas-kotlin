package com.example.facturas.data.appRepository.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class InvoiceVO(
    val state: String,
    val date: String,
    val amount: Float
) : Parcelable