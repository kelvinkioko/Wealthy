package com.expense.money.manager.ui.accounts.accounttypes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.expense.money.manager.database.models.AccountTypesEntity
import com.expense.money.manager.databinding.ItemClickableRadioBinding

class TypeAdapter(private val accountType: (AccountTypesEntity) -> Unit) :
    RecyclerView.Adapter<TypeAdapter.CurrencyViewHolder>() {

    private val items = mutableListOf<AccountTypesEntity>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyViewHolder =
        CurrencyViewHolder(
            ItemClickableRadioBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int) {
        holder.bind(items[position])
    }

    fun setAccountTypes(accountType: List<AccountTypesEntity>) {
        items.clear()
        items.addAll(accountType)
        notifyDataSetChanged()
    }

    inner class CurrencyViewHolder(
        private val binding: ItemClickableRadioBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                accountType.invoke(items[adapterPosition])
                binding.apply {
                    itemRadioButton.isChecked = !itemRadioButton.isChecked
                }
            }
        }

        fun bind(singleAccountType: AccountTypesEntity) {
            binding.apply {
                accountType.text = singleAccountType.accountTypeName

                if (singleAccountType.accountDescription.isNotEmpty()) {
                    accountTypeDescription.text = singleAccountType.accountDescription
                    accountTypeDescription.isVisible = true
                }
            }
        }
    }
}
