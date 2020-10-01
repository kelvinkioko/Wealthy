package com.expense.money.manager.ui.expenses.manageexpenses.choose

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.expense.money.manager.database.models.CategoryTypesEntity
import com.expense.money.manager.databinding.ItemClickableRadioBinding

class ChooseCategoryAdapter(private val categoryType: (CategoryTypesEntity) -> Unit) :
    RecyclerView.Adapter<ChooseCategoryAdapter.CategoryViewHolder>() {

    private val items = mutableListOf<CategoryTypesEntity>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder =
        CategoryViewHolder(
            ItemClickableRadioBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(items[position])
    }

    fun setCategories(categories: List<CategoryTypesEntity>) {
        items.clear()
        items.addAll(categories)
        notifyDataSetChanged()
    }

    inner class CategoryViewHolder(
        private val binding: ItemClickableRadioBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                categoryType.invoke(items[adapterPosition])
                binding.apply {
                    itemRadioButton.isChecked = !itemRadioButton.isChecked
                }
            }
        }

        fun bind(category: CategoryTypesEntity) {
            binding.apply {
                accountType.text = category.categoryName

                if (category.categoryDescription.isNotEmpty()) {
                    accountTypeDescription.text = category.categoryDescription
                    accountTypeDescription.isVisible = true
                }

                if (adapterPosition == itemCount - 1) {
                    accountView.isVisible = false
                }
            }
        }
    }
}
