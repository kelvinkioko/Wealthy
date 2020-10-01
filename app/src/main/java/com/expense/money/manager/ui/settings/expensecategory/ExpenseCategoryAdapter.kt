package com.expense.money.manager.ui.settings.expensecategory

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.expense.money.manager.R
import com.expense.money.manager.databinding.ItemCurrencyHeaderBinding
import com.expense.money.manager.databinding.ItemExpenseCategoryBinding

class ExpenseCategoryAdapter(private val categoryClicked: (SectionedCategoryItem.CategoryItems) -> Unit) :
    ListAdapter<SectionedCategoryItem, RecyclerView.ViewHolder>(DIFF_UTIL) {

    private val itemCategoryHeader = R.layout.item_currency_header
    private val itemCategory = R.layout.item_expense_category

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            itemCategoryHeader -> CurrencyItemHeaderViewHolder(
                ItemCurrencyHeaderBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            else -> CurrencyItemViewHolder(
                ItemExpenseCategoryBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is SectionedCategoryItem.CategoryItems -> (holder as CurrencyItemViewHolder).bind(item)
            is SectionedCategoryItem.CategoryHeader -> (holder as CurrencyItemHeaderViewHolder).bind(item)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is SectionedCategoryItem.CategoryItems -> itemCategory
            is SectionedCategoryItem.CategoryHeader -> itemCategoryHeader
        }
    }

    inner class CurrencyItemViewHolder(
        private val binding: ItemExpenseCategoryBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                categoryClicked.invoke(getItem(position) as SectionedCategoryItem.CategoryItems)
            }
        }

        fun bind(content: SectionedCategoryItem.CategoryItems) {
            binding.apply {
                categoryType.text = content.categoryName
                categoryTypeDescription.text = content.categoryDescription
                categoryTypeDescription.isVisible = true
            }
        }
    }

    inner class CurrencyItemHeaderViewHolder(
        private val binding: ItemCurrencyHeaderBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(content: SectionedCategoryItem.CategoryHeader) {
            binding.apply {
                currencyRegionHeader.text = content.title.split("#")[0]
                currencyRegionHeader.paintFlags = currencyRegionHeader.paintFlags or Paint.UNDERLINE_TEXT_FLAG
            }
        }
    }

    companion object {
        private val DIFF_UTIL = object : DiffUtil.ItemCallback<SectionedCategoryItem>() {
            override fun areItemsTheSame(old: SectionedCategoryItem, new: SectionedCategoryItem): Boolean =
                old == new

            override fun areContentsTheSame(old: SectionedCategoryItem, new: SectionedCategoryItem): Boolean =
                old == new
        }
    }
}
