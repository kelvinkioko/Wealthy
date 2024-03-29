package com.expense.money.manager.ui.expenses.daily

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.expense.money.manager.R
import com.expense.money.manager.constants.Hive
import com.expense.money.manager.constants.getShortMonthGivenNumber
import com.expense.money.manager.databinding.ItemExpenseDailyBinding
import com.expense.money.manager.databinding.ItemExpenseDailyHeaderBinding

class DailyExpensesAdapter(private val transactionClicked: (SectionedTransactionsEntity.DisplayTransactionsEntity) -> Unit) :
    ListAdapter<SectionedTransactionsEntity, RecyclerView.ViewHolder>(DIFF_UTIL) {

    private val itemDailyHeader = R.layout.item_expense_daily_header
    private val itemDaily = R.layout.item_expense_daily

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            itemDailyHeader -> DailyItemHeaderViewHolder(
                ItemExpenseDailyHeaderBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            else -> DailyItemViewHolder(
                ItemExpenseDailyBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is SectionedTransactionsEntity.DisplayTransactionsEntity -> (holder as DailyItemViewHolder).bind(item)
            is SectionedTransactionsEntity.TransactionsHeader -> (holder as DailyItemHeaderViewHolder).bind(item)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is SectionedTransactionsEntity.DisplayTransactionsEntity -> itemDaily
            is SectionedTransactionsEntity.TransactionsHeader -> itemDailyHeader
        }
    }

    inner class DailyItemViewHolder(
        private val binding: ItemExpenseDailyBinding
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

                val amount = Hive().formatCurrency(content.expenseAmount)

                transactionTitle.isVisible = true
                transactionValue.isVisible = true
                transactionTitle.text = content.expenseType.split("#")[0]
                transactionValue.text = "Ksh ${if (amount.equals(".00", ignoreCase = true)) { "0.00" } else { amount }}"
            }
        }
    }

    inner class DailyItemHeaderViewHolder(
        private val binding: ItemExpenseDailyHeaderBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(content: SectionedTransactionsEntity.TransactionsHeader) {
            binding.apply {
                val date = Hive().formatDateHeader(content.title).split("#")
                dayDateValue.text = date[1]
                monthYearValue.text = "${getShortMonthGivenNumber(date[2])}, ${date[3]}"
                saturdayValue.text = date[0]

                val totalExpense = Hive().formatCurrency(content.TotalExpense.toFloat())
                val totalIncome = Hive().formatCurrency(content.TotalIncome.toFloat())

                if (!content.TotalExpense.equals("0.0", true)) {
                    expensesTitle.isVisible = true
                    expensesValue.isVisible = true
                    expensesValue.text = "Ksh ${if (totalExpense.equals(".00", ignoreCase = true)) { "0.00" } else { totalExpense }}"
                }

                if (!content.TotalIncome.equals("0.0", true)) {
                    incomeTitle.isVisible = true
                    incomeValue.isVisible = true
                    incomeValue.text = "Ksh ${if (totalIncome.equals(".00", ignoreCase = true)) { "0.00" } else { totalIncome }}"
                }
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
