package com.money.budget.wealthy.ui.statistics

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.anychart.APIlib
import com.anychart.AnyChart
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.chart.common.listener.Event
import com.anychart.chart.common.listener.ListenersInterface
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.money.budget.wealthy.R
import com.money.budget.wealthy.constants.Hive
import com.money.budget.wealthy.constants.PreferenceHandler
import com.money.budget.wealthy.databinding.StatisticsFragmentBinding
import com.money.budget.wealthy.util.observeEvent
import com.money.budget.wealthy.util.viewBinding
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

    private var isCalendarMonthly = false

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
                val newCalendarDisplay = Hive().getCurrentYear()
                calendarMonthYear.text = newCalendarDisplay
                dateValue = newCalendarDisplay
            } else {
                val newCalendarDisplay = Hive().getCurrentMonth()
                calendarMonthYear.text = newCalendarDisplay!!.first
                PreferenceHandler(requireActivity()).setCalendarMonth("${newCalendarDisplay.first}#${newCalendarDisplay.second}")
                dateValue = newCalendarDisplay.second
            }
            viewModel.loadTransactions(transactionValue, durationValue, dateValue)
        }
    }

    private fun setupPieChart() {
        pie = AnyChart.pie()
        pie.labels().position("outside")
        pie.legend().enabled(false)

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
                    val newCalendarDisplay = Hive().getPreviousYear(calendarMonthYear.text.toString())
                    calendarMonthYear.text = newCalendarDisplay
                    dateValue = newCalendarDisplay
                } else {
                    val newCalendarDisplay = Hive().getPreviousMonth(calendarMonthYear.text.toString())
                    calendarMonthYear.text = newCalendarDisplay!!.first
                    PreferenceHandler(requireActivity()).setCalendarMonth("$newCalendarDisplay#${newCalendarDisplay.second}")
                    dateValue = newCalendarDisplay.second
                }
                viewModel.loadTransactions(transactionValue, durationValue, dateValue)
            }
            calendarFuture.setOnClickListener {
                if (isCalendarMonthly) {
                    val newCalendarDisplay = Hive().getNextYear(calendarMonthYear.text.toString())
                    calendarMonthYear.text = newCalendarDisplay
                    dateValue = newCalendarDisplay
                } else {
                    val newCalendarDisplay = Hive().getNextMonth(calendarMonthYear.text.toString())
                    calendarMonthYear.text = newCalendarDisplay!!.first
                    PreferenceHandler(requireActivity()).setCalendarMonth("$newCalendarDisplay#${newCalendarDisplay.second}")
                    dateValue = newCalendarDisplay.second
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
                    viewModel.loadTransactions(transactionValue, durationValue, dateValue)
                    when (durationValue) {
                        "Monthly" -> {
                            isCalendarMonthly = false
                            setupData()
                        }
                        "Yearly" -> {
                            isCalendarMonthly = true
                            setupData()
                        }
                        "Periodically" -> { }
                    }
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
        populateStatisticsList(statisticsExpenseItem)
        val data: MutableList<DataEntry> = ArrayList()
        val loopCount = if (statisticsExpenseItem.isEmpty()) 1 else statisticsExpenseItem.size
        for (x in 0 until loopCount) {
            if (statisticsExpenseItem.isEmpty())
                data.add(ValueDataEntry("$x", 100))
            else
                data.add(ValueDataEntry(statisticsExpenseItem[x].expenseCategory.split("#")[0], statisticsExpenseItem[x].expenseAmount))
        }

        pie.data(data)
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
