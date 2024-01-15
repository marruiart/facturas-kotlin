package com.example.facturas.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.example.facturas.R
import com.example.facturas.databinding.FragmentInvoicesListBinding
import com.example.facturas.ui.adapters.InvoicesListAdapter
import com.example.facturas.ui.viewmodels.InvoicesListViewModel
import com.google.android.material.color.MaterialColors

class InvoicesListFragment : Fragment() {
    private lateinit var binding: FragmentInvoicesListBinding
    private val viewModel: InvoicesListViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentInvoicesListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val toolbar = binding.toolbar
        toolbar.inflateMenu(R.menu.invoices_list_toolbar_menu)
        // Change the navigation icon color
        toolbar.navigationIcon?.setTint(MaterialColors.getColor(view, com.google.android.material.R.attr.colorPrimary))
        toolbar.setNavigationOnClickListener {
            Toast.makeText(context, context?.getString(R.string.not_available_yet), Toast.LENGTH_SHORT).show()
        }
        val adapter = InvoicesListAdapter()
        bindView(adapter)
    }

    private fun bindView(adapter: InvoicesListAdapter) {
        val rv = binding.invoicesRv
        rv.adapter = adapter
        val list = viewModel.getAllInvoices()
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