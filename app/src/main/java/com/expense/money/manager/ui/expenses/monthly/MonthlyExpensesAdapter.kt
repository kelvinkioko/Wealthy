package com.expense.money.manager.ui.expenses.monthly

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.expense.money.manager.constants.Hive
import com.expense.money.manager.databinding.ItemExpenseMonthlyBinding

class MonthlyExpensesAdapter(private val monthlyTransactions: (MonthlyTransactionsEntity) -> Unit) :
    RecyclerView.Adapter<MonthlyExpensesAdapter.MonthlyViewHolder>() {

    private val items = mutableListOf<MonthlyTransactionsEntity>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonthlyViewHolder =
        MonthlyViewHolder(
            ItemExpenseMonthlyBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: MonthlyViewHolder, position: Int) {
        holder.bind(items[position])
    }

    fun setMonthlyTransactions(monthlyTransactions: List<MonthlyTransactionsEntity>) {
        items.clear()
        items.addAll(monthlyTransactions)
        notifyDataSetChanged()
    }

    inner class MonthlyViewHolder(
        private val binding: ItemExpenseMonthlyBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(content: MonthlyTransactionsEntity) {
            binding.apply {
                monthValue.text = content.MonthName
                transactionValue.text = content.Transactions

                expensesTitle.isVisible = true
                expensesValue.isVisible = true
                expensesValue.text = "Ksh ${Hive().formatCurrency(content.TotalExpense.toFloat())}"

                incomeTitle.isVisible = true
                incomeValue.isVisible = true
                incomeValue.text = "Ksh ${Hive().formatCurrency(content.TotalIncome.toFloat())}"
            }
        }
    }
}
