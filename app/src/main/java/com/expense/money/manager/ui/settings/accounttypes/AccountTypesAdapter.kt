package com.expense.money.manager.ui.settings.accounttypes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.expense.money.manager.databinding.ItemAccountTypeBinding

class AccountTypesAdapter(private val accountClicked: (AccountTypesWithActionsEntity) -> Unit) :
    RecyclerView.Adapter<AccountTypesAdapter.AccountTypesViewHolder>() {

    private val items = mutableListOf<AccountTypesWithActionsEntity>()

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

    fun setAccountTypes(accountTypes: List<AccountTypesWithActionsEntity>) {
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

        fun bind(accountItem: AccountTypesWithActionsEntity) {
            binding.apply {
                accountType.text = accountItem.accountTypeName

                if (accountItem.accountTypeDescription.isNotEmpty()) {
                    accountTypeDescription.text = accountItem.accountTypeDescription
                    accountTypeDescription.isVisible = true
                }

                accountTypeEdit.setOnClickListener { accountItem.editAccountTypeClick.invoke() }
                accountTypeDelete.setOnClickListener { accountItem.deleteAccountTypeClick.invoke() }
            }
        }
    }
}
