package com.money.budget.wealthy.ui.expenses

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayout
import com.money.budget.wealthy.R
import com.money.budget.wealthy.databinding.ExpensesFragmentBinding
import com.money.budget.wealthy.util.debouncedClick
import com.money.budget.wealthy.util.viewBinding

class ExpensesFragment : Fragment(R.layout.expenses_fragment) {

    private val binding by viewBinding(ExpensesFragmentBinding::bind)

    private val mainNavController: NavController by lazy { requireActivity().findNavController(R.id.expensesHostFragment) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupTabLayout()
    }

    private fun setupTabLayout() {
        binding.apply {
            topNavigation.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

                override fun onTabSelected(tab: TabLayout.Tab?) {
                    // Handle tab select
                    tab!!.position
                    when (tab.position) {
                        0 -> mainNavController.navigate(R.id.toDailyExpensesFragment)
                        1 -> mainNavController.navigate(R.id.toWeeklyExpensesFragment)
                        2 -> mainNavController.navigate(R.id.toMonthlyExpensesFragment)
                        3 -> mainNavController.navigate(R.id.toCalendarExpensesFragment)
                        else -> println("selected tab, Click not supported")
                    }
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                    println("reselected tab, ${tab!!.position}")
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                    // Handle tab unselect
                }
            })

            addTransaction.debouncedClick(lifecycleScope) {
                findNavController().navigate(ExpensesFragmentDirections.toAddExpenseFragment())
            }
        }
    }
}
