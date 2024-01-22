package com.example.facturas.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.example.facturas.R
import com.example.facturas.databinding.FragmentInvoicesFilterBinding
import com.example.facturas.services.FilterService
import com.example.facturas.services.models.Filter
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.TimeZone

class InvoicesFilterFragment : Fragment() {
    private lateinit var binding: FragmentInvoicesFilterBinding
    private lateinit var filterSvc: FilterService
    private lateinit var filter: Filter
    private lateinit var tmpFilter: Filter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentInvoicesFilterBinding.inflate(inflater, container, false)
        initFilter()
        Log.d("DEBUG FILTER", filter.toString())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setToolbar()
        setClickListeners()
        setFilterState()
    }

    private fun initFilter() {
        filterSvc = FilterService.getInstance()
        filter = filterSvc.filter
        tmpFilter = filter.copy()
    }

    private fun setToolbar() {
        setMenuListeners(binding.appTopBar.toolbar)
    }

    private fun setMenuListeners(toolbar: Toolbar) {
        toolbar.setNavigationOnClickListener {
            navigateBack()
        }
    }

    private fun navigateBack() {
        requireActivity().supportFragmentManager.popBackStack()
    }

    private fun setClickListeners() {
        binding.issueDateFromDate.setOnClickListener {
            showDatePickerDialog(true)
        }
        binding.issueDateToDate.setOnClickListener {
            showDatePickerDialog(false)
        }
        binding.checkBoxPaid.setOnCheckedChangeListener { _, isChecked ->
            tmpFilter.state.paid = isChecked
        }
        binding.checkBoxCancelled.setOnCheckedChangeListener { _, isChecked ->
            tmpFilter.state.cancelled = isChecked
        }
        binding.checkBoxFixedFee.setOnCheckedChangeListener { _, isChecked ->
            tmpFilter.state.fixedFee = isChecked
        }
        binding.checkBoxPending.setOnCheckedChangeListener { _, isChecked ->
            tmpFilter.state.pending = isChecked
        }
        binding.checkBoxPaymentPlan.setOnCheckedChangeListener { _, isChecked ->
            tmpFilter.state.paymentPlan = isChecked
        }
        binding.applyButton.setOnClickListener {
            Log.d("DEBUG FILTER", tmpFilter.toString())
            filter = tmpFilter.copy()
        }
    }

    private fun setFilterState() {
        setDatesState()
        binding.checkBoxPaid.isChecked = tmpFilter.state.paid
        binding.checkBoxCancelled.isChecked = tmpFilter.state.cancelled
        binding.checkBoxFixedFee.isChecked = tmpFilter.state.fixedFee
        binding.checkBoxPending.isChecked = tmpFilter.state.pending
        binding.checkBoxPaymentPlan.isChecked = tmpFilter.state.paymentPlan
    }

    private fun setDatesState() {
        setDateFromState()
        setDateToState()
    }

    private fun setDateFromState() {
        val date = tmpFilter.dateFrom
        binding.issueDateFromDate.text =
            if (date == null) getString(R.string.invoices_filter_default_date) else formatDate(date)
    }

    private fun setDateToState() {
        val date = tmpFilter.dateTo
        binding.issueDateToDate.text =
            if (date == null) getString(R.string.invoices_filter_default_date) else formatDate(date)
    }

    private fun showDatePickerDialog(isFromValue: Boolean) {
        val constraints = getCalendarConstraints(isFromValue)
        val datePicker = buildDatePicker(constraints)

        datePicker.show(requireActivity().supportFragmentManager, "DATE_PICKER")
        datePicker.addOnPositiveButtonClickListener { selectedDate ->
            if (isFromValue) {
                tmpFilter.dateFrom = parseToLocalDate(selectedDate)
                setDateFromState()
            } else {
                tmpFilter.dateTo = parseToLocalDate(selectedDate)
                setDateToState()
            }
        }
    }

    private fun buildDatePicker(constraints: CalendarConstraints): MaterialDatePicker<Long> {
        return MaterialDatePicker.Builder.datePicker()
            .setTitleText(getString(R.string.invoices_filter_pick_date))
            .setCalendarConstraints(constraints).build()
    }

    private fun getCalendarConstraints(isFromValue: Boolean): CalendarConstraints {
        return if (isFromValue) {
            CalendarConstraints.Builder().setOpenAt(getJanuaryThisYear())
                .setValidator(DateValidatorPointBackward.now()).build()
        } else {
            CalendarConstraints.Builder().setOpenAt(MaterialDatePicker.todayInUtcMilliseconds())
                .setValidator(DateValidatorPointBackward.now()).build()
        }
    }

    private fun getJanuaryThisYear(): Long {
        val today = MaterialDatePicker.todayInUtcMilliseconds()
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))

        calendar.timeInMillis = today
        calendar[Calendar.MONTH] = Calendar.JANUARY
        return calendar.timeInMillis
    }

    private fun parseToLocalDate(value: Long): LocalDate =
        Instant.ofEpochMilli(value).atZone(ZoneId.systemDefault()).toLocalDate()

    private fun formatDate(localDate: LocalDate): String {
        val format = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        return localDate.format(format)
    }
}

