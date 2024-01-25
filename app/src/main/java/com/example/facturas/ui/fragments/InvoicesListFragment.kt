package com.example.facturas.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import com.example.facturas.R
import com.example.facturas.data.appRepository.models.InvoiceVO
import com.example.facturas.databinding.FragmentInvoicesListBinding
import com.example.facturas.ui.adapters.InvoicesListAdapter
import com.example.facturas.ui.viewmodels.InvoicesListViewModel
import com.example.facturas.utils.AppEnvironment
import com.google.android.material.color.MaterialColors
import kotlinx.coroutines.launch
import kotlin.math.ceil
import kotlin.math.floor

class InvoicesListFragment : Fragment() {
    private lateinit var binding: FragmentInvoicesListBinding
    private val viewModel: InvoicesListViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentInvoicesListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setToolbar(view)
        bindView()
    }

    private fun setToolbar(view: View) {
        val toolbar = binding.toolbar
        toolbar.inflateMenu(R.menu.invoices_list_toolbar_menu)
        setNavigationIconColor(toolbar, view, com.google.android.material.R.attr.colorPrimary)
        setMenuIconColor(toolbar, view, com.google.android.material.R.attr.colorOnBackground)
        setMenuListeners(toolbar)
    }

    private fun bindView() {
        val adapter = InvoicesListAdapter(::onInvoiceClick, viewModel.appContext)
        setEnvironmentSwitchListener()
        setRecyclerViewAdapter(adapter)
        populateInvoicesList(adapter)
    }

    private fun setEnvironmentSwitchListener() {
        binding.switchEnvironment.setOnCheckedChangeListener { _, isChecked ->
            var environment: String
            if (isChecked) {
                environment = AppEnvironment.PROD_ENVIRONMENT
                Toast.makeText(context, "Entorno cambiado a producción", Toast.LENGTH_SHORT).show()
            } else {
                environment = AppEnvironment.MOCK_ENVIRONMENT
                Toast.makeText(
                    context, "Entorno cambiado a desarrollo (mock data)", Toast.LENGTH_SHORT
                ).show()
            }
            viewModel.setRepository(environment)
        }
    }

    private fun setRecyclerViewAdapter(adapter: InvoicesListAdapter) {
        binding.invoicesRv.adapter = adapter
    }

    private fun populateInvoicesList(adapter: InvoicesListAdapter) {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.invoices.collect { list ->
                    Log.d("DEBUG LIST", list.toString())
                    if (list.isEmpty()) {
                        binding.emptyRv.visibility = View.VISIBLE
                        binding.invoicesRv.visibility = View.GONE
                    } else {
                        adapter.submitList(list)
                        binding.emptyRv.visibility = View.GONE
                        binding.invoicesRv.visibility = View.VISIBLE
                        updateFilterAmountRange(list)
                        Log.d("FILTER UPDATE SELECTED", viewModel.filter.toString())
                    }
                }
            }
        }
    }

    private fun updateFilterAmountRange(list: List<InvoiceVO>) {
        var max: Float = Float.MIN_VALUE
        var min: Float = Float.MAX_VALUE
        list.forEach { invoice ->
            val amountCeil = ceil(invoice.amount)
            val amountFloor = floor(invoice.amount)
            max = if (amountCeil > max) amountCeil else max
            min = if (amountFloor < min) amountFloor else min
        }
        viewModel.filter.setAmountRange(min, max)
        if (viewModel.filter.selectedAmount.max != null && viewModel.filter.selectedAmount.max!! > max) {
            Log.d(
                "LIST FILTER",
                "Change MAX. Dirty: ${!viewModel.filter.isDirty} Over: ${viewModel.filter.selectedAmount.max != null && viewModel.filter.selectedAmount.max!! > max}"
            )
            viewModel.filter.selectedAmount.max = null
        }
        if (viewModel.filter.selectedAmount.min != null && viewModel.filter.selectedAmount.min!! < min) {
            Log.d(
                "LIST FILTER",
                "Change MIN. Dirty: ${!viewModel.filter.isDirty} Below: ${viewModel.filter.selectedAmount.min != null && viewModel.filter.selectedAmount.min!! < min}"
            )
            viewModel.filter.selectedAmount.min = null
        }
    }

    private fun setNavigationIconColor(toolbar: Toolbar, view: View, color: Int) {
        toolbar.navigationIcon?.setTint(
            MaterialColors.getColor(
                view, color
            )
        )
    }

    private fun setMenuIconColor(toolbar: Toolbar, view: View, color: Int) {
        toolbar.menu.children.forEach {
            it.icon?.setTint(MaterialColors.getColor(view, color))
        }
    }

    private fun setMenuListeners(toolbar: Toolbar) {
        toolbar.setNavigationOnClickListener {
            Toast.makeText(
                context, context?.getString(R.string.not_available_yet), Toast.LENGTH_SHORT
            ).show()
        }

        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_filter -> {
                    val action =
                        InvoicesListFragmentDirections.actionInvoicesListFragmentToInvoicesFilterFragment()
                    requireView().findNavController().navigate(action)
                    true
                }

                else -> false
            }
        }
    }

    private fun onInvoiceClick(invoice: InvoiceVO) {
        createAlertDialog().show()
        Log.d("DEBUG CLICK_INVOICE", invoice.toString())
    }

    private fun createAlertDialog(): AlertDialog {
        val builder: AlertDialog.Builder = requireActivity().let {
            AlertDialog.Builder(it)
        }
        builder.setMessage(R.string.not_available_yet).setTitle(R.string.information)
            .setNegativeButton(R.string.close) { dialog, _ ->
                dialog.cancel()
            }
        return builder.create()
    }
}