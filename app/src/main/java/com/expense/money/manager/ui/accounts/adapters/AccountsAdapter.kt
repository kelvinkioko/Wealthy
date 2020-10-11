package com.expense.money.manager.ui.accounts.adapters

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.expense.money.manager.R
import com.expense.money.manager.constants.Hive
import com.expense.money.manager.databinding.ItemAccountBinding
import com.expense.money.manager.databinding.ItemAccountHeaderBinding
import com.expense.money.manager.ui.accounts.SectionedAccountDetailsItem

class AccountsAdapter(private val accountClicked: (SectionedAccountDetailsItem.AccountsEntity) -> Unit) :
    ListAdapter<SectionedAccountDetailsItem, RecyclerView.ViewHolder>(DIFF_UTIL) {

    private val itemAccountHeader = R.layout.item_account_header
    private val itemAccount = R.layout.item_account

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            itemAccountHeader -> AccountItemHeaderViewHolder(
                ItemAccountHeaderBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            else -> AccountItemViewHolder(
                ItemAccountBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is SectionedAccountDetailsItem.AccountsEntity -> (holder as AccountItemViewHolder).bind(item)
            is SectionedAccountDetailsItem.AccountsHeader -> (holder as AccountItemHeaderViewHolder).bind(item)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is SectionedAccountDetailsItem.AccountsEntity -> itemAccount
            is SectionedAccountDetailsItem.AccountsHeader -> itemAccountHeader
        }
    }

    inner class AccountItemViewHolder(
        private val binding: ItemAccountBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                accountClicked.invoke(getItem(position) as SectionedAccountDetailsItem.AccountsEntity)
            }
        }

        fun bind(account: SectionedAccountDetailsItem.AccountsEntity) {
            binding.apply {
                accountName.text = account.sourceName
                accountBalance.text = "Kes ${Hive().formatCurrency(account.sourceBalance.toFloat())}"
                accountBalance.isVisible = true
            }
        }
    }

    inner class AccountItemHeaderViewHolder(
        private val binding: ItemAccountHeaderBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(content: SectionedAccountDetailsItem.AccountsHeader) {
            binding.apply {
                accountHeader.text = content.title
                accountHeader.paintFlags = accountHeader.paintFlags or Paint.UNDERLINE_TEXT_FLAG
            }
        }
    }

    companion object {
        private val DIFF_UTIL = object : DiffUtil.ItemCallback<SectionedAccountDetailsItem>() {
            override fun areItemsTheSame(old: SectionedAccountDetailsItem, new: SectionedAccountDetailsItem): Boolean =
                old == new

            override fun areContentsTheSame(old: SectionedAccountDetailsItem, new: SectionedAccountDetailsItem): Boolean =
                old == new
        }
    }
}
