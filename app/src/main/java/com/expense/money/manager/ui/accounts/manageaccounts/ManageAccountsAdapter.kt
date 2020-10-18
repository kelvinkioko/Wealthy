package com.expense.money.manager.ui.accounts.manageaccounts

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.expense.money.manager.R
import com.expense.money.manager.databinding.ItemAccountHeaderBinding
import com.expense.money.manager.databinding.ItemAccountManageBinding
import com.expense.money.manager.ui.accounts.SectionedAccountItem

class ManageAccountsAdapter :
    ListAdapter<SectionedAccountItem, RecyclerView.ViewHolder>(DIFF_UTIL) {

    private val itemAccountHeader = R.layout.item_account_header
    private val itemAccount = R.layout.item_account_manage

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
                ItemAccountManageBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is SectionedAccountItem.ManageAccountsEntity -> (holder as AccountItemViewHolder).bind(item)
            is SectionedAccountItem.ManageAccountsHeader -> (holder as AccountItemHeaderViewHolder).bind(item)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is SectionedAccountItem.ManageAccountsEntity -> itemAccount
            is SectionedAccountItem.ManageAccountsHeader -> itemAccountHeader
        }
    }

    inner class AccountItemViewHolder(
        private val binding: ItemAccountManageBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(account: SectionedAccountItem.ManageAccountsEntity) {
            binding.apply {
                accountName.text = account.sourceName
                accountDescription.text = account.sourceDescription
                accountDescription.isVisible = true

                accountEdit.setOnClickListener { account.editAccountClick.invoke() }
                accountDelete.setOnClickListener { account.deleteAccountClick.invoke() }
            }
        }
    }

    inner class AccountItemHeaderViewHolder(
        private val binding: ItemAccountHeaderBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(content: SectionedAccountItem.ManageAccountsHeader) {
            binding.apply {
                accountHeader.text = content.title
                accountHeader.paintFlags = accountHeader.paintFlags or Paint.UNDERLINE_TEXT_FLAG
            }
        }
    }

    companion object {
        private val DIFF_UTIL = object : DiffUtil.ItemCallback<SectionedAccountItem>() {
            override fun areItemsTheSame(old: SectionedAccountItem, new: SectionedAccountItem): Boolean =
                old == new

            override fun areContentsTheSame(old: SectionedAccountItem, new: SectionedAccountItem): Boolean =
                old == new
        }
    }
}
