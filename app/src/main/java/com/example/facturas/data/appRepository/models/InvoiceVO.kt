package com.example.facturas.data.appRepository.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDate

@Parcelize
data class InvoiceVO(
    val stateResource: Int,
    val date: LocalDate,
    val amount: Float
) : Parcelable