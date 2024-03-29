package com.expense.money.manager.ui.expenses

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.expense.money.manager.R
import com.expense.money.manager.constants.Hive
import com.expense.money.manager.constants.PreferenceHandler
import com.expense.money.manager.databinding.ExpensesFragmentBinding
import com.expense.money.manager.util.viewBinding
import com.google.android.material.tabs.TabLayout

class ExpensesFragment : Fragment(R.layout.expenses_fragment) {

    private val binding by viewBinding(ExpensesFragmentBinding::bind)

    private val mainNavController: NavController by lazy { requireActivity().findNavController(R.id.expensesHostFragment) }

    private lateinit var sharedViewModel: SharedExpenseViewModel

    private var isCalendarMonthly = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedViewModel = requireActivity().run { ViewModelProvider(this).get(SharedExpenseViewModel::class.java) }

        setupTabLayout()
        setupData()
        setupClickListeners()
    }

    private fun setupData() {
        binding.apply {
            if (isCalendarMonthly) {
                val newCalendarDisplay = Hive().getCurrentYear()
                calendarMonthYear.text = newCalendarDisplay
                sharedViewModel.setToolbarYearCalendar(newCalendarDisplay)
            } else {
                val newCalendarDisplay = Hive().getCurrentMonth()
                calendarMonthYear.text = newCalendarDisplay!!.first
                sharedViewModel.setToolbarMonthCalendar(newCalendarDisplay.second)
                PreferenceHandler(requireActivity()).setCalendarMonth("${newCalendarDisplay.first}#${newCalendarDisplay.second}")
            }
        }
    }

    private fun setupClickListeners() {
        binding.apply {
            calendarPast.setOnClickListener {
                if (isCalendarMonthly) {
                    val newCalendarDisplay = Hive().getCurrentYear(calendarMonthYear.text.toString(), toPast = true)
                    calendarMonthYear.text = newCalendarDisplay
                    sharedViewModel.setToolbarYearCalendar(newCalendarDisplay)
                } else {
                    val newCalendarDisplay = Hive().getCurrentMonth(calendarMonthYear.text.toString(), toPast = true)
                    calendarMonthYear.text = newCalendarDisplay!!.first
                    sharedViewModel.setToolbarMonthCalendar(newCalendarDisplay.second)
                    PreferenceHandler(requireActivity()).setCalendarMonth("$newCalendarDisplay#${newCalendarDisplay.second}")
                }
            }
            calendarFuture.setOnClickListener {
                if (isCalendarMonthly) {
                    val newCalendarDisplay = Hive().getCurrentYear(calendarMonthYear.text.toString(), toFuture = true)
                    calendarMonthYear.text = newCalendarDisplay
                    sharedViewModel.setToolbarYearCalendar(newCalendarDisplay)
                } else {
                    val newCalendarDisplay = Hive().getCurrentMonth(calendarMonthYear.text.toString(), toFuture = true)
                    calendarMonthYear.text = newCalendarDisplay!!.first
                    sharedViewModel.setToolbarMonthCalendar(newCalendarDisplay.second)
                    PreferenceHandler(requireActivity()).setCalendarMonth("$newCalendarDisplay#${newCalendarDisplay.second}")
                }
            }
            addTransaction.setOnClickListener {
                findNavController().navigate(ExpensesFragmentDirections.toAddExpenseFragment())
            }
        }
    }

    private fun setupTabLayout() {
        binding.apply {
            topNavigation.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

                override fun onTabSelected(tab: TabLayout.Tab?) {
                    // Handle tab select
                    tab!!.position
                    when (tab.position) {
                        0 -> {
                            isCalendarMonthly = false
                            setupData()
                            mainNavController.navigate(R.id.toDailyExpensesFragment)
                        }
                        1 -> {
                            isCalendarMonthly = false
                            setupData()
                            mainNavController.navigate(R.id.toWeeklyExpensesFragment)
                        }
                        2 -> {
                            isCalendarMonthly = true
                            setupData()
                            mainNavController.navigate(R.id.toMonthlyExpensesFragment)
                        }
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
        }
    }
}
