package com.example.facturas.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.facturas.R
import com.example.facturas.databinding.FragmentInvoicesFilterBinding
import com.example.facturas.services.models.Filter
import com.example.facturas.services.models.Range
import com.example.facturas.ui.viewmodels.InvoicesListViewModel
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.slider.RangeSlider
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.TimeZone
import kotlin.math.ceil
import kotlin.math.floor

class InvoicesFilterFragment : Fragment() {
    private lateinit var binding: FragmentInvoicesFilterBinding
    private val viewModel: InvoicesListViewModel by activityViewModels()
    private lateinit var tmpFilter: Filter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentInvoicesFilterBinding.inflate(inflater, container, false)
        initTmpFilter()
        Log.d("FILTER DEBUG", tmpFilter.toString())
        return binding.root
    }

    private fun initTmpFilter() {
        tmpFilter = viewModel.filter.copy()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
        setFilterInitialLayout()
    }

    private fun setListeners() {
        setClickListeners()
        setSliderTouchListener()
        setCheckboxesChangeListeners()
    }

    private fun setFilterInitialLayout() {
        setFromAndToDatesLayout()
        setAmountsLayout()
        setCheckboxesLayout()
    }

    private fun setClickListeners() {
        setToolbarMenuListeners(binding.appTopBar.toolbar)
        setFromAndToDatePickerListeners()
        setFooterButtonsListeners()
    }

    private fun setToolbarMenuListeners(toolbar: Toolbar) {
        toolbar.setNavigationOnClickListener {
            navigateBack()
        }
    }

    private fun navigateBack() {
        requireActivity().supportFragmentManager.popBackStack()
    }

    private fun setFromAndToDatePickerListeners() {
        setDatePickerListener(binding.issueDateFromDate)
        setDatePickerListener(binding.issueDateToDate)
    }

    private fun setDatePickerListener(pickerBtn: Button) {
        pickerBtn.setOnClickListener {
            showDatePickerDialog(it.id == R.id.issue_date_from_date)
        }
    }

    private fun setFooterButtonsListeners() {
        binding.applyButton.setOnClickListener {
            viewModel.filter = tmpFilter.copy()
            navigateBack()
        }
        binding.removeButton.setOnClickListener {
            tmpFilter = Filter(
                amountRange = Range(tmpFilter.amountRange.min, tmpFilter.amountRange.max)
            )
            setFilterInitialLayout()
        }
    }

    private fun setSliderTouchListener() {
        binding.amountSlider.addOnSliderTouchListener(object : RangeSlider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: RangeSlider) {
                // empty on purpose
            }

            override fun onStopTrackingTouch(slider: RangeSlider) {
                val min = slider.values[0]
                val max = slider.values[1]
                Log.d(
                    "FILTER SELECTED MIN",
                    "${slider.values[0]} == ${tmpFilter.amountRange.min} ? ${slider.values[0] == tmpFilter.amountRange.min}"
                )
                Log.d(
                    "FILTER SELECTED MAX",
                    "${slider.values[1]} == ${tmpFilter.amountRange.max} ? ${slider.values[1] == tmpFilter.amountRange.max}"
                )
                val selectedMin =
                    if (slider.values[0] == tmpFilter.amountRange.min) null else slider.values[0]
                val selectedMax =
                    if (slider.values[1] == tmpFilter.amountRange.max) null else slider.values[1]
                tmpFilter.setSelectedAmounts(selectedMin, selectedMax)
                setMiddleAmountsLayout(min.toInt(), max.toInt())
                tmpFilter.isDirty = true
            }
        })
    }

    private fun setCheckboxesChangeListeners() {
        binding.checkBoxPaid.setOnCheckedChangeListener { _, isChecked ->
            tmpFilter.state.paid = isChecked
            tmpFilter.isDirty = true
        }
        binding.checkBoxCancelled.setOnCheckedChangeListener { _, isChecked ->
            tmpFilter.state.cancelled = isChecked
            tmpFilter.isDirty = true
        }
        binding.checkBoxFixedFee.setOnCheckedChangeListener { _, isChecked ->
            tmpFilter.state.fixedFee = isChecked
            tmpFilter.isDirty = true
        }
        binding.checkBoxPending.setOnCheckedChangeListener { _, isChecked ->
            tmpFilter.state.pending = isChecked
            tmpFilter.isDirty = true
        }
        binding.checkBoxPaymentPlan.setOnCheckedChangeListener { _, isChecked ->
            tmpFilter.state.paymentPlan = isChecked
            tmpFilter.isDirty = true
        }
    }

    // AMOUNT SLIDER LAYOUT

    private fun setAmountsLayout() {
        val min = floor(tmpFilter.amountRange.min)
        val max = ceil(tmpFilter.amountRange.max)
        val selectedMin = floor(tmpFilter.selectedAmount.min ?: min)
        val selectedMax = ceil(tmpFilter.selectedAmount.max ?: max)

        binding.minRange.text = getString(R.string.invoices_filter_amount_range, min.toInt())
        binding.maxRange.text = getString(R.string.invoices_filter_amount_range, max.toInt())
        binding.amountSlider.valueFrom = min
        binding.amountSlider.valueTo = max
        setMiddleAmountsLayout(selectedMin.toInt(), selectedMax.toInt())
        binding.amountSlider.setValues(selectedMin, selectedMax)
    }

    private fun setMiddleAmountsLayout(min: Int?, max: Int?) {
        binding.middleRange.text = getString(
            R.string.invoices_filter_selected_amount_range, min, max
        )
    }

    // DATES LAYOUT

    private fun setFromAndToDatesLayout() {
        setDateLayout(binding.issueDateFromDate, tmpFilter.dates.from)
        setDateLayout(binding.issueDateToDate, tmpFilter.dates.to)
    }

    private fun setDateLayout(dateButton: Button, date: LocalDate?) {
        dateButton.text =
            if (date == null) getString(R.string.invoices_filter_default_date) else formatDate(date)
    }

    private fun formatDate(localDate: LocalDate): String {
        val format = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        return localDate.format(format)
    }

    // CHECKBOXES LAYOUT

    private fun setCheckboxesLayout() {
        binding.checkBoxPaid.isChecked = tmpFilter.state.paid
        binding.checkBoxCancelled.isChecked = tmpFilter.state.cancelled
        binding.checkBoxFixedFee.isChecked = tmpFilter.state.fixedFee
        binding.checkBoxPending.isChecked = tmpFilter.state.pending
        binding.checkBoxPaymentPlan.isChecked = tmpFilter.state.paymentPlan
    }

    // DATEPICKER

    private fun showDatePickerDialog(isDateFrom: Boolean) {
        val constraints = getCalendarConstraints(isDateFrom)
        val datePicker = buildDatePicker(constraints)

        datePicker.show(requireActivity().supportFragmentManager, "DATE_PICKER")
        datePicker.addOnPositiveButtonClickListener { selectedDate ->
            if (isDateFrom) {
                tmpFilter.dates.from = parseToLocalDate(selectedDate)
                setDateLayout(binding.issueDateFromDate, tmpFilter.dates.from)
            } else {
                tmpFilter.dates.to = parseToLocalDate(selectedDate)
                setDateLayout(binding.issueDateToDate, tmpFilter.dates.to)
            }
            tmpFilter.isDirty = true
        }
    }

    private fun getCalendarConstraints(isDateFrom: Boolean): CalendarConstraints {
        val openValue =
            if (isDateFrom) getJanuaryThisYear() else MaterialDatePicker.todayInUtcMilliseconds()
        return CalendarConstraints.Builder().setOpenAt(openValue)
            .setValidator(DateValidatorPointBackward.now()).build()
    }

    private fun buildDatePicker(constraints: CalendarConstraints): MaterialDatePicker<Long> {
        return MaterialDatePicker.Builder.datePicker()
            .setTitleText(getString(R.string.invoices_filter_pick_date))
            .setCalendarConstraints(constraints).build()
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

}

