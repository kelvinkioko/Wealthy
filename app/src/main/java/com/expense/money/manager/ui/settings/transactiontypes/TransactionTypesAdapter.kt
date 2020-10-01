package com.expense.money.manager.ui.settings.transactiontypes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.expense.money.manager.database.models.TransactionTypesEntity
import com.expense.money.manager.databinding.ItemTransactionTypeBinding

class TransactionTypesAdapter(private val transactionType: (TransactionTypesEntity) -> Unit) :
    RecyclerView.Adapter<TransactionTypesAdapter.TransactionViewHolder>() {

    private val items = mutableListOf<TransactionTypesEntity>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder =
        TransactionViewHolder(
            ItemTransactionTypeBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        holder.bind(items[position])
    }

    fun setTransactionTypes(transactionType: List<TransactionTypesEntity>) {
        items.clear()
        items.addAll(transactionType)
        notifyDataSetChanged()
    }

    inner class TransactionViewHolder(
        private val binding: ItemTransactionTypeBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(transaction: TransactionTypesEntity) {
            binding.apply {
                transactionType.text = transaction.transactionName
                transactionTypeDescription.text = transaction.transactionDescription
                transactionTypeDescription.isVisible = true
            }
        }
    }
}
