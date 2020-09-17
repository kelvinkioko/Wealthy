package com.money.budget.wealthy.ui.settings.currency

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.money.budget.wealthy.R
import com.money.budget.wealthy.database.models.AccountTypesEntity
import com.money.budget.wealthy.databinding.ItemCurrencyBinding
import com.money.budget.wealthy.databinding.ItemCurrencyHeaderBinding

class CurrencyAdapter : ListAdapter<SectionedCurrencyItem, RecyclerView.ViewHolder>(DIFF_UTIL) {

    private val items = mutableListOf<AccountTypesEntity>()

    private val itemCurrencyHeader = R.layout.item_currency_header
    private val itemCurrency = R.layout.item_currency

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            itemCurrencyHeader -> CurrencyItemHeaderViewHolder(
                ItemCurrencyHeaderBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            else -> CurrencyItemViewHolder(
                ItemCurrencyBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is SectionedCurrencyItem.CurrencyItems -> (holder as CurrencyItemViewHolder).bind(item)
            is SectionedCurrencyItem.CurrencyHeader -> (holder as CurrencyItemHeaderViewHolder).bind(item)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is SectionedCurrencyItem.CurrencyItems -> itemCurrency
            is SectionedCurrencyItem.CurrencyHeader -> itemCurrencyHeader
        }
    }

    inner class CurrencyItemViewHolder(
        private val binding: ItemCurrencyBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(content: SectionedCurrencyItem.CurrencyItems) {
            binding.apply {
                currencyName.text = content.country
                currencyDetails.text = "${content.currency} | ${content.currencyCode} | ${content.currencySymbol}"
            }
        }
    }

    inner class CurrencyItemHeaderViewHolder(
        private val binding: ItemCurrencyHeaderBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(content: SectionedCurrencyItem.CurrencyHeader) {
            binding.apply {
                currencyRegionHeader.text = content.title
            }
        }
    }

    companion object {
        private val DIFF_UTIL = object : DiffUtil.ItemCallback<SectionedCurrencyItem>() {
            override fun areItemsTheSame(old: SectionedCurrencyItem, new: SectionedCurrencyItem): Boolean =
                old == new

            override fun areContentsTheSame(old: SectionedCurrencyItem, new: SectionedCurrencyItem): Boolean =
                old == new
        }
    }
}
