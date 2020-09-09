package com.money.budget.wealthy.ui.settings.accounttypes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.money.budget.wealthy.database.models.AccountTypesEntity
import com.money.budget.wealthy.databinding.ItemAccountTypeBinding

class AccountTypesAdapter(private val accountClicked: (AccountTypesEntity) -> Unit) :
    RecyclerView.Adapter<AccountTypesAdapter.AccountTypesViewHolder>() {

    private val items = mutableListOf<AccountTypesEntity>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountTypesViewHolder =
        AccountTypesViewHolder(
            ItemAccountTypeBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: AccountTypesViewHolder, position: Int) {
        holder.bind(items[position])
    }

    fun setAccountTypes(accountTypes: List<AccountTypesEntity>) {
        items.clear()
        items.addAll(accountTypes)
        notifyDataSetChanged()
    }

    inner class AccountTypesViewHolder(
        private val binding: ItemAccountTypeBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                accountClicked.invoke(items[adapterPosition])
            }
        }

        fun bind(accountItem: AccountTypesEntity) {
            binding.apply {
                accountType.text = accountItem.accountTypeName

                if (accountItem.accountDescription.isNotEmpty()) {
                    accountTypeDescription.text = accountItem.accountDescription
                    accountTypeDescription.isVisible = true
                }

                if (adapterPosition == itemCount - 1) {
                    accountTypeView.isVisible = false
                }
            }
        }
    }
}
