package com.expense.money.manager.ui.expenses.weekly

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.expense.money.manager.R
import com.expense.money.manager.constants.Hive
import com.expense.money.manager.databinding.ItemExpenseWeeklyBinding
import com.expense.money.manager.databinding.ItemExpenseWeeklyHeaderBinding

class WeeklyExpensesAdapter(private val transactionClicked: (SectionedTransactionsEntity.DisplayTransactionsEntity) -> Unit) :
    ListAdapter<SectionedTransactionsEntity, RecyclerView.ViewHolder>(DIFF_UTIL) {

    private val itemWeeklyHeader = R.layout.item_expense_weekly_header
    private val itemWeekly = R.layout.item_expense_weekly

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            itemWeeklyHeader -> WeeklyItemHeaderViewHolder(
                ItemExpenseWeeklyHeaderBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            else -> WeeklyItemViewHolder(
                ItemExpenseWeeklyBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is SectionedTransactionsEntity.DisplayTransactionsEntity -> (holder as WeeklyItemViewHolder).bind(item)
            is SectionedTransactionsEntity.TransactionsHeader -> (holder as WeeklyItemHeaderViewHolder).bind(item)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is SectionedTransactionsEntity.DisplayTransactionsEntity -> itemWeekly
            is SectionedTransactionsEntity.TransactionsHeader -> itemWeeklyHeader
        }
    }

    inner class WeeklyItemViewHolder(
        private val binding: ItemExpenseWeeklyBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                transactionClicked.invoke(getItem(position) as SectionedTransactionsEntity.DisplayTransactionsEntity)
            }
        }

        fun bind(content: SectionedTransactionsEntity.DisplayTransactionsEntity) {
            binding.apply {
                transactionNameValue.text = content.expenseName
                transactionCategoryValue.text = "${content.expenseCategory.split("#")[0]} - ${content.expenseAccount.split("#")[0]}"

                if (content.expenseType.contains("expense", true)) {
                    transactionTitle.setTextColor(transactionTitle.context.getColor(R.color.colorNegative))
                } else if (content.expenseType.contains("income", true)) {
                    transactionTitle.setTextColor(transactionTitle.context.getColor(R.color.colorPositive))
                }

                transactionTitle.isVisible = true
                transactionValue.isVisible = true
                transactionTitle.text = content.expenseType.split("#")[0]
                transactionValue.text = "Ksh ${Hive().formatCurrency(content.expenseAmount)}"
            }
        }
    }

    inner class WeeklyItemHeaderViewHolder(
        private val binding: ItemExpenseWeeklyHeaderBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(content: SectionedTransactionsEntity.TransactionsHeader) {
            binding.apply {
                weekCount.text = "Week ${content.weekPosition} of 52"
                rangeValue.text = content.range
                transactionValue.text = content.transactions

                expensesTitle.isVisible = true
                expensesValue.isVisible = true
                expensesValue.text = "Ksh ${Hive().formatCurrency(content.TotalExpense.toFloat())}"

                incomeTitle.isVisible = true
                incomeValue.isVisible = true
                incomeValue.text = "Ksh ${Hive().formatCurrency(content.TotalIncome.toFloat())}"

//                if ((content.TotalIncome.equals("0.0", true) || content.TotalIncome.equals("0", true)) &&
//                    (content.TotalExpense.equals("0.0", true) || content.TotalExpense.equals("0", true))) {
//                    headerViewTop.isVisible = true
//                    headerViewBottom.isVisible = true
//                } else {
//                    headerViewTop.isVisible = true
//                    headerViewBottom.isVisible = true
//                    headerViewAlternateTop.isGone = true
//                    headerViewAlternateBottom.isGone = true
//                }
            }
        }
    }

    companion object {
        private val DIFF_UTIL = object : DiffUtil.ItemCallback<SectionedTransactionsEntity>() {
            override fun areItemsTheSame(old: SectionedTransactionsEntity, new: SectionedTransactionsEntity): Boolean =
                old == new

            override fun areContentsTheSame(old: SectionedTransactionsEntity, new: SectionedTransactionsEntity): Boolean =
                old == new
        }
    }
}
