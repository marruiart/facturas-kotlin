package com.example.facturas.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import com.example.facturas.databinding.FragmentInvoicesFilterBinding

class InvoicesFilterFragment : Fragment() {
    private lateinit var binding: FragmentInvoicesFilterBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentInvoicesFilterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setToolbar()
    }

    private fun setToolbar() {
        val toolbar = binding.toolbar
        setMenuListeners(toolbar)
    }

    private fun setMenuListeners(toolbar: Toolbar) {
        toolbar.setNavigationOnClickListener {
            navigateBack()
        }
    }

    private fun navigateBack() {
        requireActivity().supportFragmentManager.popBackStack()
    }
}