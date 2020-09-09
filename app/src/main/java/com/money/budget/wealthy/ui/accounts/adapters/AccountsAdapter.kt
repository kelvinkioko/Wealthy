package com.money.budget.wealthy.ui.accounts.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.money.budget.wealthy.database.models.AccountsEntity
import com.money.budget.wealthy.databinding.ItemAccountHeaderBinding
import com.money.budget.wealthy.databinding.ItemAccountManageBinding
import com.money.budget.wealthy.util.SectioningAdapter
import java.util.ArrayList

class AccountsAdapter(private val accounts: (AccountsEntity) -> Unit) : SectioningAdapter() {

    private val sections = ArrayList<Section>()

    private lateinit var itemBinding: ItemAccountManageBinding
    private lateinit var headerBinding: ItemAccountHeaderBinding

    inner class Section {
        var alpha: String = ""
        var items: ArrayList<AccountsEntity> = ArrayList()
    }

    override fun onCreateItemViewHolder(parent: ViewGroup, itemType: Int): ItemViewHolder =
        ItemViewHolder(ItemAccountManageBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onCreateHeaderViewHolder(parent: ViewGroup, headerType: Int): HeaderViewHolder =
        HeaderViewHolder(ItemAccountHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun getNumberOfSections(): Int = sections.size

    override fun getNumberOfItemsInSection(sectionIndex: Int): Int = sections[sectionIndex].items.size

    override fun doesSectionHaveHeader(sectionIndex: Int): Boolean = true

    override fun doesSectionHaveFooter(sectionIndex: Int): Boolean = false

    inner class ItemViewHolder(binding: ItemAccountManageBinding) : SectioningAdapter.ItemViewHolder(binding.root) {
        init {
            itemBinding = binding
        }
    }

    inner class HeaderViewHolder(binding: ItemAccountHeaderBinding) : SectioningAdapter.HeaderViewHolder(binding.root) {
        init {
            headerBinding = binding
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onBindHeaderViewHolder(holder: SectioningAdapter.HeaderViewHolder, sectionIndex: Int, headerType: Int) {
        val section = sections[sectionIndex]

        headerBinding.apply {
            headerTitle.text = section.alpha

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
            accountName.text = account.sourceName
            accountDescription.text = account.sourceDescription
            accountDescription.isVisible = true

            if ((itemIndex + 1) == section.items.size) {
                accountView.isGone = true
            }
        }
    }

    fun setAccounts(accounts: List<AccountsEntity>) {
        sections.clear()

        var alpha: String
        var secAlpha = ""
        var currentSection: Section? = null
        for (account in accounts) {
            alpha = account.sourceType
            when {
                secAlpha.isEmpty() -> {
                    secAlpha = account.sourceType

                    currentSection = Section()
                    currentSection.alpha = account.sourceType
                    currentSection.items.add(account)

                    if (currentSection != null) {
                        sections.add(currentSection)
                    }
                }
                secAlpha.equals(alpha, true) -> currentSection!!.items.add(account)
                else -> {
                    secAlpha = account.sourceType

                    currentSection = Section()
                    currentSection.alpha = account.sourceType
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
