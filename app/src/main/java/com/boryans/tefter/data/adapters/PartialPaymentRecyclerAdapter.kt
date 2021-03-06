package com.boryans.tefter.data.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.boryans.tefter.R
import com.boryans.tefter.data.model.entities.PartialPayment
import com.boryans.tefter.utils.OwesSharedPrefs.initSharedPrefs
import com.boryans.tefter.utils.OwesSharedPrefs.readFromPrefs
import kotlinx.android.synthetic.main.item_partial_payment.view.*

class PartialPaymentRecyclerAdapter: RecyclerView.Adapter<PartialPaymentRecyclerAdapter.PartialPaymentViewHolder>() {


    private val differCallback = object: DiffUtil.ItemCallback<PartialPayment>() {
        override fun areItemsTheSame(oldItem: PartialPayment, newItem: PartialPayment): Boolean {
            return oldItem.paymentId == newItem.paymentId
        }

        override fun areContentsTheSame(oldItem: PartialPayment, newItem: PartialPayment): Boolean {
            return oldItem.paymentId === newItem.paymentId
        }
    }

    val partialPDiffer = AsyncListDiffer(this, differCallback)

    inner class PartialPaymentViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PartialPaymentViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context).inflate(R.layout.item_partial_payment, parent, false)
        return PartialPaymentViewHolder(layoutInflater)
    }

    override fun onBindViewHolder(holder: PartialPaymentViewHolder, position: Int) {
        initSharedPrefs(holder.itemView.context)
        val curr = readFromPrefs(holder.itemView.context.getString(R.string.CURRENCY), holder.itemView.context.getString(R.string.DOLLAR))
        val partialPayment = partialPDiffer.currentList[position]

        val partialAmount = String.format("%.2f", partialPayment.amount).toDouble()
        holder.itemView.apply {
            datePartialPayment.text = partialPayment.date
            amountPartialPayment.text = "$curr${partialAmount}"
        }
    }

    override fun getItemCount() = partialPDiffer.currentList.size
}