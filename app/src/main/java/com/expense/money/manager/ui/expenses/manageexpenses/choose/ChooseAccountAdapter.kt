package com.expense.money.manager.ui.expenses.manageexpenses.choose

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.expense.money.manager.database.models.AccountsEntity
import com.expense.money.manager.databinding.ItemClickableRadioBinding

class ChooseAccountAdapter(private val accountClicked: (AccountsEntity) -> Unit) :
    RecyclerView.Adapter<ChooseAccountAdapter.AccountViewHolder>() {

    private val items = mutableListOf<AccountsEntity>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountViewHolder =
        AccountViewHolder(
            ItemClickableRadioBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: AccountViewHolder, position: Int) {
        holder.bind(items[position])
    }

    fun setAccounts(accounts: List<AccountsEntity>) {
        items.clear()
        items.addAll(accounts)
        notifyDataSetChanged()
    }

    inner class AccountViewHolder(
        private val binding: ItemClickableRadioBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                accountClicked.invoke(items[adapterPosition])
                binding.apply {
                    itemRadioButton.isChecked = !itemRadioButton.isChecked
                }
            }
        }

        fun bind(account: AccountsEntity) {
            binding.apply {
                accountType.text = account.sourceName

                if (account.sourceDescription.isNotEmpty()) {
                    accountTypeDescription.text = account.sourceDescription
                    accountTypeDescription.isVisible = true
                }

                if (adapterPosition == itemCount - 1) {
                    accountView.isVisible = false
                }
            }
        }
    }
}
