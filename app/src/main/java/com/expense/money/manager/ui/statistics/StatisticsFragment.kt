package com.expense.money.manager.ui.statistics

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.anychart.APIlib
import com.anychart.AnyChart
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.chart.common.listener.Event
import com.anychart.chart.common.listener.ListenersInterface
import com.expense.money.manager.R
import com.expense.money.manager.constants.Hive
import com.expense.money.manager.constants.PreferenceHandler
import com.expense.money.manager.databinding.StatisticsFragmentBinding
import com.expense.money.manager.util.observeEvent
import com.expense.money.manager.util.viewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.util.ArrayList
import org.koin.androidx.viewmodel.ext.android.viewModel

class StatisticsFragment : Fragment(R.layout.statistics_fragment) {

    private val binding by viewBinding(StatisticsFragmentBinding::bind)

    private val statisticsAdapter: StatisticsAdapter by lazy {
        StatisticsAdapter { onStatisticsExpenseItemPicked(it) }
    }

    private fun onStatisticsExpenseItemPicked(expense: StatisticsExpenseItem) {
    }

    private val viewModel: StatisticsViewModel by viewModel()

    private var isCalendarMonthly = true

    private var transactionValue: String = ""
    private var durationValue: String = ""
    private var dateValue: String = ""
    private lateinit var pie: com.anychart.charts.Pie

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupClickListeners()
        setupObservers()
        setupStatisticsList()
        setupPieChart()
    }

    private fun setupData() {
        binding.apply {
            if (isCalendarMonthly) {
                val newCalendarDisplay = Hive().getCurrentMonth()
                calendarMonthYear.text = newCalendarDisplay!!.first
                PreferenceHandler(requireActivity()).setCalendarMonth("${newCalendarDisplay.first}#${newCalendarDisplay.second}")
                dateValue = newCalendarDisplay.second
            } else {
                val newCalendarDisplay = Hive().getCurrentYear()
                calendarMonthYear.text = newCalendarDisplay
                dateValue = newCalendarDisplay
            }
            viewModel.loadTransactions(transactionValue, durationValue, dateValue)
        }
    }

    private fun setupPieChart() {
        pie = AnyChart.pie()
        pie.labels().position("outside")
        pie.legend().enabled(false)
        pie.background().enabled(true)
        pie.background().fill(getString(R.string.chart_color))

        pie.setOnClickListener(object :
            ListenersInterface.OnClickListener(arrayOf("x", "value")) {
            override fun onClick(event: Event) {}
        })

        binding.apply {
            APIlib.getInstance().setActiveAnyChartView(anyChartView)
            anyChartView.setChart(pie)
        }
    }

    private fun setupStatisticsList() {
        binding.statisticsList.adapter = statisticsAdapter
    }

    private fun populateStatisticsList(statisticsExpenseItem: List<StatisticsExpenseItem>) {
        statisticsAdapter.setStatisticsExpense(statisticsExpenseItem)
    }

    private fun setupClickListeners() {
        binding.apply {
            calendarPast.setOnClickListener {
                if (isCalendarMonthly) {
                    val newCalendarDisplay = Hive().getCurrentMonth(calendarMonthYear.text.toString(), toPast = true)
                    calendarMonthYear.text = newCalendarDisplay!!.first
                    PreferenceHandler(requireActivity()).setCalendarMonth("$newCalendarDisplay#${newCalendarDisplay.second}")
                    dateValue = newCalendarDisplay.second
                } else {
                    val newCalendarDisplay = Hive().getCurrentYear(calendarMonthYear.text.toString(), toPast = true)
                    calendarMonthYear.text = newCalendarDisplay
                    dateValue = newCalendarDisplay
                }
                viewModel.loadTransactions(transactionValue, durationValue, dateValue)
            }
            calendarFuture.setOnClickListener {
                if (isCalendarMonthly) {
                    val newCalendarDisplay = Hive().getCurrentMonth(calendarMonthYear.text.toString(), toFuture = true)
                    calendarMonthYear.text = newCalendarDisplay!!.first
                    PreferenceHandler(requireActivity()).setCalendarMonth("$newCalendarDisplay#${newCalendarDisplay.second}")
                    dateValue = newCalendarDisplay.second
                } else {
                    val newCalendarDisplay = Hive().getCurrentYear(calendarMonthYear.text.toString(), toFuture = true)
                    calendarMonthYear.text = newCalendarDisplay
                    dateValue = newCalendarDisplay
                }
                viewModel.loadTransactions(transactionValue, durationValue, dateValue)
            }
            transactionType.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    transactionValue = transactionType.selectedItem.toString()
                    viewModel.loadTransactions(transactionValue, durationValue, dateValue)
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {}
            }
            durationType.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    durationValue = durationType.selectedItem.toString()
                    when (durationValue) {
                        "Monthly" -> {
                            isCalendarMonthly = true
                            setupData()
                        }
                        "Yearly" -> {
                            isCalendarMonthly = false
                            setupData()
                        }
//                        "Periodically" -> { }
                    }
                    viewModel.loadTransactions(transactionValue, durationValue, dateValue)
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {}
            }
        }
    }

    private fun setupObservers() {
        viewModel.uiState.observe(viewLifecycleOwner) {
            when (it) {
                is StatisticsUIState.TransactionTypes -> setupSpinnerAdapter(it.transactionTypesEntity)
                is StatisticsUIState.Transactions -> loadPieChart(it.statisticsExpenseItem)
            }
        }
        viewModel.action.observeEvent(viewLifecycleOwner) {
            when (it) {
                is StatisticsActions.Navigate -> findNavController().navigate(it.destination)
                is StatisticsActions.BottomNavigate -> showDialog(it.bottomSheetDialogFragment)
            }
        }
    }

    private fun showDialog(bottomSheetDialogFragment: BottomSheetDialogFragment) {
        bottomSheetDialogFragment.show(parentFragmentManager, bottomSheetDialogFragment.tag)
    }

    private fun loadPieChart(statisticsExpenseItem: List<StatisticsExpenseItem>) {
        if (statisticsExpenseItem.isEmpty())
            binding.apply {
                // Show empty
                emptyState.emptyParent.isVisible = true
                emptyState.emptyText.text = String.format(getString(R.string.empty_statistics_string), transactionType.selectedItem.toString(), calendarMonthYear.text.toString())
                // Hide chart & recycler
                anyChartView.isGone = true
                statisticsList.isGone = true
            }
        else {
            binding.apply {
                // Hide empty
                emptyState.emptyParent.isGone = true
                // Show chart & recycler
                anyChartView.isVisible = true
                statisticsList.isVisible = true
            }
            populateStatisticsList(statisticsExpenseItem)
            val data: MutableList<DataEntry> = ArrayList()
            val loopCount = if (statisticsExpenseItem.isEmpty()) 1 else statisticsExpenseItem.size
            for (x in 0 until loopCount) {
                if (statisticsExpenseItem.isEmpty())
                    data.add(ValueDataEntry("$x", 100))
                else
                    data.add(
                        ValueDataEntry(
                            statisticsExpenseItem[x].expenseCategory.split("#")[0],
                            statisticsExpenseItem[x].expenseAmount
                        )
                    )
            }

            pie.data(data)
        }
    }

    private fun setupSpinnerAdapter(values: List<String>) {
        binding.apply {
            transactionType.apply {
                this.context ?: return@apply
                this.adapter = object : ArrayAdapter<Any>(
                    requireContext(),
                    R.layout.item_spinner,
                    values
                ) {
                    @SuppressLint("NewApi")
                    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                        return super.getDropDownView(position, convertView, parent).also {
                            when {
                                position == this@apply.selectedItemPosition -> {
                                    it.setBackgroundColor(resources.getColor(R.color.colorPrimaryDark, null))
                                }
                                position % 2 == 0 -> {
                                    it.setBackgroundColor(resources.getColor(R.color.colorPrimary, null))
                                }
                                else -> {
                                    it.setBackgroundColor(resources.getColor(R.color.colorPrimary, null))
                                }
                            }
                        }
                    }
                    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                        return super.getView(position, convertView, parent).apply {
                            setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom)
                        }
                    }
                }
            }

            durationType.apply {
                this.context ?: return@apply
                this.adapter = object : ArrayAdapter<Any>(
                    requireContext(),
                    R.layout.item_spinner,
                    resources.getStringArray(R.array.durationType)
                ) {
                    @SuppressLint("NewApi")
                    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                        return super.getDropDownView(position, convertView, parent).also {
                            when {
                                position == this@apply.selectedItemPosition -> {
                                    it.setBackgroundColor(resources.getColor(R.color.colorPrimaryDark, null))
                                }
                                position % 2 == 0 -> {
                                    it.setBackgroundColor(resources.getColor(R.color.colorPrimary, null))
                                }
                                else -> {
                                    it.setBackgroundColor(resources.getColor(R.color.colorPrimary, null))
                                }
                            }
                        }
                    }
                    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                        return super.getView(position, convertView, parent).apply {
                            setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom)
                        }
                    }
                }
            }

            transactionValue = transactionType.selectedItem.toString()
            durationValue = durationType.selectedItem.toString()
            setupData()
        }
    }
}
