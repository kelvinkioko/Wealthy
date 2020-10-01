package com.expense.money.manager.ui.statistics

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.expense.money.manager.databinding.ItemStatisticsAverageBinding

class StatisticsAdapter(private val statisticsExpense: (StatisticsExpenseItem) -> Unit) :
    RecyclerView.Adapter<StatisticsAdapter.TransactionViewHolder>() {

    private val items = mutableListOf<StatisticsExpenseItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder =
        TransactionViewHolder(
            ItemStatisticsAverageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        holder.bind(items[position])
    }

    fun setStatisticsExpense(statisticsExpense: List<StatisticsExpenseItem>) {
        items.clear()
        items.addAll(statisticsExpense)
        notifyDataSetChanged()
    }

    inner class TransactionViewHolder(
        private val binding: ItemStatisticsAverageBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(expense: StatisticsExpenseItem) {
            binding.apply {
                statisticsNameValue.text = expense.expenseCategory.split("#")[0]
                statisticsCostValue.text = "${expense.expenseAmount} Ksh"
            }
        }
    }
}
