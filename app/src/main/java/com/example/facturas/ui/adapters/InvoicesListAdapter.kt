package com.example.facturas.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.facturas.data.appRepository.models.InvoiceVO
import com.example.facturas.databinding.InvoicesListItemBinding
import com.example.facturas.utils.App
import com.example.facturas.utils.Dates

class InvoicesListAdapter(
    private val onInvoiceClick: ((invoice: InvoiceVO) -> Unit)
) : ListAdapter<InvoiceVO, InvoicesListAdapter.InvoicesListViewHolder>(InvoiceDiffCallBack()) {

    inner class InvoicesListViewHolder(private val binding: InvoicesListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val item = binding.invoiceItem
        private val amount = binding.itemAmount
        private val date = binding.itemDate
        private val state = binding.itemState

        fun bindView(invoice: InvoiceVO) {
            date.text = invoice.date.format(Dates.FORMATTER)
            state.text = App.context.getString(invoice.stateResource)
            amount.text = invoice.amount.toString()
            item.setOnClickListener {
                onInvoiceClick(invoice)
            }
        }
    }

    private class InvoiceDiffCallBack : DiffUtil.ItemCallback<InvoiceVO>() {
        override fun areItemsTheSame(oldItem: InvoiceVO, newItem: InvoiceVO): Boolean =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: InvoiceVO, newItem: InvoiceVO): Boolean =
            oldItem == newItem
    }

    /**
     * Create new views (invoked by the layout manager)
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InvoicesListViewHolder {
        val binding =
            InvoicesListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return InvoicesListViewHolder(binding)
    }

    /**
     * Replace the contents of a view (invoked by the layout manager)
     */
    override fun onBindViewHolder(holder: InvoicesListViewHolder, position: Int) {
        val invoice = getItem(position)
        holder.bindView(invoice)
    }
}