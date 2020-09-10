package com.money.budget.wealthy.ui.expenses.manageexpenses

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.money.budget.wealthy.database.models.TransactionTypesEntity
import com.money.budget.wealthy.databinding.ItemTransactionTypeBinding

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

        init {
            itemView.setOnClickListener {
                transactionType.invoke(items[adapterPosition])
                binding.apply {
                    itemRadioButton.isChecked = !itemRadioButton.isChecked
                }
            }
        }

        fun bind(transactionType: TransactionTypesEntity) {
            binding.apply {
                transactionName.text = transactionType.transactionName
            }
        }
    }
}
