package com.money.budget.wealthy.ui.expenses.daily

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.money.budget.wealthy.constants.Hive
import com.money.budget.wealthy.database.models.ExpensesEntity
import com.money.budget.wealthy.databinding.ItemExpenseDailyBinding
import com.money.budget.wealthy.databinding.ItemExpenseDailyHeaderBinding
import com.money.budget.wealthy.util.SectioningAdapter
import java.util.ArrayList

class DailyExpensesAdapter : SectioningAdapter() {

    private val sections = ArrayList<Section>()

    private lateinit var itemBinding: ItemExpenseDailyBinding
    private lateinit var headerBinding: ItemExpenseDailyHeaderBinding

    inner class Section {
        var alpha: String = ""
        var items: ArrayList<ExpensesEntity> = ArrayList()
    }

    override fun onCreateItemViewHolder(parent: ViewGroup, itemType: Int): ItemViewHolder =
        ItemViewHolder(ItemExpenseDailyBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onCreateHeaderViewHolder(parent: ViewGroup, headerType: Int): HeaderViewHolder =
        HeaderViewHolder(ItemExpenseDailyHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun getNumberOfSections(): Int = sections.size

    override fun getNumberOfItemsInSection(sectionIndex: Int): Int = sections[sectionIndex].items.size

    override fun doesSectionHaveHeader(sectionIndex: Int): Boolean = true

    override fun doesSectionHaveFooter(sectionIndex: Int): Boolean = false

    inner class ItemViewHolder(binding: ItemExpenseDailyBinding) : SectioningAdapter.ItemViewHolder(binding.root) {
        init {
            itemBinding = binding
        }
    }

    inner class HeaderViewHolder(binding: ItemExpenseDailyHeaderBinding) : SectioningAdapter.HeaderViewHolder(binding.root) {
        init {
            headerBinding = binding
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onBindHeaderViewHolder(holder: SectioningAdapter.HeaderViewHolder, sectionIndex: Int, headerType: Int) {
        val section = sections[sectionIndex]

        headerBinding.apply {
            val date = Hive().formatDateHeader(section.alpha).split("#")
            dayDateValue.text = date[1]
            monthYearValue.text = date[2] + "." + date[3]
            saturdayValue.text = date[0]

            if (sectionIndex == 0) {
                headerViewTop.isGone = true
            }
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onBindItemViewHolder(viewHolder: SectioningAdapter.ItemViewHolder, sectionIndex: Int, itemIndex: Int, itemType: Int) {
        val section = sections[sectionIndex]

        val account = section.items[itemIndex]

        itemBinding.apply {
            expenseNameValue.text = account.expenseName
            expenseCategoryValue.text = "${account.expenseCategory.split("#")[0]} - ${account.expenseAccount.split("#")[0]}"

            if (account.expenseType.contains("expense", true)) {
                expensesTitle.isVisible = true
                expensesValue.isVisible = true
                expensesValue.text = "Ksh ${Hive().formatCurrency(account.expenseAmount.toInt())}"
            } else {
                incomeTitle.isVisible = true
                incomeValue.isVisible = true
                incomeValue.text = "Ksh ${Hive().formatCurrency(account.expenseAmount.toInt())}"
            }

            if ((itemIndex + 1) == section.items.size) {
                expensesDailyView.isGone = true
            }
        }
    }

    fun setExpenses(accounts: List<ExpensesEntity>) {
        sections.clear()

        var alpha: String
        var secAlpha = ""
        var currentSection: Section? = null
        for (account in accounts) {
            alpha = account.expenseDate
            when {
                secAlpha.isEmpty() -> {
                    secAlpha = account.expenseDate

                    currentSection = Section()
                    currentSection.alpha = account.expenseDate
                    currentSection.items.add(account)

                    if (currentSection != null) {
                        sections.add(currentSection)
                    }
                }
                secAlpha.equals(alpha, true) -> currentSection!!.items.add(account)
                else -> {
                    secAlpha = account.expenseDate

                    currentSection = Section()
                    currentSection.alpha = account.expenseDate
                    currentSection.items.add(account)

                    if (currentSection != null) {
                        sections.add(currentSection)
                    }
                }
            }
        }

        notifyAllSectionsDataSetChanged()
    }
}
