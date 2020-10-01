package com.expense.money.manager.ui.expenses.weekly

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavDirections
import com.expense.money.manager.constants.Hive
import com.expense.money.manager.database.repository.ExpensesRepository
import com.expense.money.manager.util.Event
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class WeeklyExpensesViewModel(
    private val expensesRepository: ExpensesRepository
) : ViewModel() {

    private val _uiState = MutableLiveData<WeeklyExpensesUIState>()
    val uiState: LiveData<WeeklyExpensesUIState> = _uiState

    private val _action = MutableLiveData<Event<WeeklyExpensesActions>>()
    val action: LiveData<Event<WeeklyExpensesActions>> = _action

    fun loadExpenses(monthYear: String) {
        val splitMonth = monthYear.split("/")
        val ranges = Hive().getWeeksOfMonth(month = splitMonth[0].toInt(), year = splitMonth[1].toInt())
        val sectionedTransactionsItem = mutableListOf<SectionedTransactionsEntity>()

        ranges.forEach { range ->
            val transactions = expensesRepository.loadExpenses(Hive().getDateFromString(range.startDate), Hive().getDateFromString(range.endDate))
            println("Range ${transactions.size}")

            if (transactions.isNotEmpty()) {
                var previousTransaction = ""
                transactions.forEach {
                    val currentTransaction = Hive().getStringFromDate(it.expenseDate)
                    if (previousTransaction.isNullOrEmpty() || previousTransaction != currentTransaction) {
                        previousTransaction = currentTransaction
                        sectionedTransactionsItem.add(
                            SectionedTransactionsEntity.TransactionsHeader(
                                title = currentTransaction,
                                range = "${range.startDate.replace("/${splitMonth[1]}", "")} - ${range.endDate.replace("/${splitMonth[1]}", "")}",
                                weekPosition = range.weekPosition,
                                transactions = expensesRepository.countExpensesByRange(Hive().getDateFromString(range.startDate), Hive().getDateFromString(range.endDate)).toString() + " Transactions",
                                TotalExpense = expensesRepository.loadExpensesByDate(it.expenseDate).toString(),
                                TotalIncome = expensesRepository.loadIncomeByDate(it.expenseDate).toString()
                            )
                        )
                    }
                    sectionedTransactionsItem.add(
                        SectionedTransactionsEntity.DisplayTransactionsEntity(
                            expenseID = it.expenseID,
                            expenseType = it.expenseType,
                            expenseName = it.expenseName,
                            expenseAmount = it.expenseAmount,
                            expenseAccount = it.expenseAccount,
                            expenseCategory = it.expenseCategory,
                            expenseDescription = it.expenseDescription,
                            expenseImage = it.expenseImage,
                            expenseDate = Hive().getStringFromDate(it.expenseDate),
                            createdAt = it.createdAt
                        )
                    )
                }
            } else {
                sectionedTransactionsItem.add(
                    SectionedTransactionsEntity.TransactionsHeader(
                        title = "",
                        range = "${range.startDate.replace("/${splitMonth[1]}", "")} - ${range.endDate.replace("/${splitMonth[1]}", "")}",
                        weekPosition = range.weekPosition,
                        transactions = "0 Transaction(s)",
                        TotalExpense = "0",
                        TotalIncome = "0"
                    )
                )
            }
        }

        _uiState.postValue(WeeklyExpensesUIState.WeeklyExpenses(sectionedTransactionsItem))
    }
}

sealed class WeeklyExpensesActions {
    data class BottomNavigate(val bottomSheetDialogFragment: BottomSheetDialogFragment) : WeeklyExpensesActions()

    data class Navigate(val destination: NavDirections) : WeeklyExpensesActions()
}

sealed class WeeklyExpensesUIState {
    object Loading : WeeklyExpensesUIState()

    object Success : WeeklyExpensesUIState()

    data class WeeklyExpenses(val expensesEntity: List<SectionedTransactionsEntity>) : WeeklyExpensesUIState()

    data class Error(val statusCode: String, val message: String) : WeeklyExpensesUIState()
}

sealed class SectionedTransactionsEntity {
    data class DisplayTransactionsEntity(
        var expenseID: String,
        var expenseType: String,
        var expenseName: String,
        var expenseAmount: Float,
        var expenseAccount: String,
        var expenseCategory: String,
        var expenseDescription: String,
        var expenseImage: String,
        var expenseDate: String,
        var createdAt: String
    ) : SectionedTransactionsEntity()

    data class TransactionsHeader(
        val title: String,
        val range: String,
        val weekPosition: String,
        val transactions: String,
        var TotalExpense: String,
        var TotalIncome: String
    ) : SectionedTransactionsEntity()
}
