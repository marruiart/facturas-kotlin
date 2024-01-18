package com.example.facturas.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
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

class InvoicesListFragment : Fragment() {
    private lateinit var binding: FragmentInvoicesListBinding
    private val viewModel: InvoicesListViewModel by viewModels()
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
        viewModel.initRepository()
        bindView()
    }

    private fun bindView() {
        val adapter = InvoicesListAdapter(::onInvoiceClick)
        setEnvironmentSwitchListener()
        setRecyclerViewAdapter(adapter)
        populateInvoicesList(adapter)
    }

    private fun setToolbar(view: View) {
        val toolbar = binding.toolbar
        toolbar.inflateMenu(R.menu.invoices_list_toolbar_menu)
        setNavigationIconColor(toolbar, view, com.google.android.material.R.attr.colorPrimary)
        setMenuListeners(toolbar)
    }

    private fun setEnvironmentSwitchListener() {
        binding.switchEnvironment.setOnCheckedChangeListener { _, isChecked ->
            var environment: String
            if (isChecked) {
                environment = AppEnvironment.PROD_ENVIRONMENT
                Toast.makeText(context, "Entorno cambiado a producción", Toast.LENGTH_SHORT).show()
            } else {
                environment = AppEnvironment.MOCK_ENVIRONMENT
                Toast.makeText(context, "Entorno cambiado a desarrollo (mock data)", Toast.LENGTH_SHORT).show()
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
                    Log.d("LIST", list.toString())
                    if (list.isEmpty()) {
                        binding.emptyRv.visibility = View.VISIBLE
                        binding.invoicesRv.visibility = View.GONE
                    } else {
                        adapter.submitList(list)
                        binding.emptyRv.visibility = View.GONE
                        binding.invoicesRv.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun setNavigationIconColor(toolbar: Toolbar, view: View, color: Int) {
        toolbar.navigationIcon?.setTint(
            MaterialColors.getColor(
                view, color
            )
        )
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
        Log.d("CLICK_INVOICE", invoice.toString())
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