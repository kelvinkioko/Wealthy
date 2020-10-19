package com.expense.money.manager.ui.expenses.manageexpenses

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.expense.money.manager.database.models.TransactionTypesEntity
import com.expense.money.manager.databinding.ItemTransactionTypeSelectorBinding

class TransactionTypesAdapter(private val transactionType: (TransactionTypesEntity) -> Unit) :
    RecyclerView.Adapter<TransactionTypesAdapter.TransactionViewHolder>() {

    private val items = mutableListOf<TransactionTypesEntity>()

    private var checkedItems = ""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder =
        TransactionViewHolder(
            ItemTransactionTypeSelectorBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        holder.bind(items[position])
    }

    fun setTransactionTypes(transactionType: List<TransactionTypesEntity>, selected: String = "") {
        items.clear()
        items.addAll(transactionType)
        this.checkedItems = selected
        notifyDataSetChanged()
    }

    inner class TransactionViewHolder(
        private val binding: ItemTransactionTypeSelectorBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                transactionType.invoke(items[adapterPosition])
                val radioItem = items[adapterPosition]
                checkedItems = if (checkedItems.contains(radioItem.transactionName)) { " " } else { radioItem.transactionName }
                binding.apply {
                    itemRadioButton.isChecked = checkedItems.contains(radioItem.transactionName)
                    notifyDataSetChanged()
                }
            }
        }

        fun bind(transactionType: TransactionTypesEntity) {
            binding.apply {
                transactionName.text = transactionType.transactionName
                if (checkedItems.isNotEmpty()) {
                    itemRadioButton.isChecked = checkedItems.contains(transactionType.transactionName)
                }
            }
        }
    }
}
