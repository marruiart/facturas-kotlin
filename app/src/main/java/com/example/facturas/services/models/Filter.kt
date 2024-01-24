package com.example.facturas.services.models

import java.time.LocalDate

data class Filter(
    var isDirty: Boolean = false,
    var dates: SelectedDate = SelectedDate(),
    var selectedAmount: SelectedRange = SelectedRange(),
    var amountRange: Range = Range(),
    var state: StateCheckboxes = StateCheckboxes()
) {
    fun setAmountRange(min: Float, max: Float) {
        this.amountRange.min = min
        this.amountRange.max = max
    }

    fun setSelectedAmounts(min: Float?, max: Float?) {
        this.selectedAmount.min = min
        this.selectedAmount.max = max
    }
}

data class StateCheckboxes(
    var paid: Boolean = false,
    var cancelled: Boolean = false,
    var fixedFee: Boolean = false,
    var pending: Boolean = false,
    var paymentPlan: Boolean = false
)

data class Range(
    var min: Float = 1.0f, var max: Float = 100.0f
)

data class SelectedRange(
    var min: Float? = null, var max: Float? = null
)

data class SelectedDate(
    var from: LocalDate? = null,
    var to: LocalDate? = null,
)
