package com.example.facturas.services.models

import com.example.facturas.utils.Dates
import java.time.LocalDate

data class Filter(
    var dateFrom: LocalDate? = null,
    var dateTo: LocalDate? = null,
    var maxSelectedAmount: Double = 100.0,
    var maxRangeAmount: Double = 100.0,
    var minSelectedAmount: Double = 1.0,
    var minRangeAmount: Double = 1.0,
    var state: StateCheckboxes = StateCheckboxes()
)

data class StateCheckboxes(
    var paid: Boolean = false,
    var cancelled: Boolean = false,
    var fixedFee: Boolean = false,
    var pending: Boolean = false,
    var paymentPlan: Boolean = false
)
