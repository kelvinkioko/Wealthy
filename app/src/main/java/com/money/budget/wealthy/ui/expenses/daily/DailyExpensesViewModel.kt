package com.money.budget.wealthy.ui.expenses.daily

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavDirections
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.money.budget.wealthy.constants.Hive
import com.money.budget.wealthy.database.repository.ExpensesRepository
import com.money.budget.wealthy.util.Event

class DailyExpensesViewModel(
    private val expensesRepository: ExpensesRepository
) : ViewModel() {

    private val _uiState = MutableLiveData<DailyExpensesUIState>()
    val uiState: LiveData<DailyExpensesUIState> = _uiState

    private val _action = MutableLiveData<Event<DailyExpensesActions>>()
    val action: LiveData<Event<DailyExpensesActions>> = _action

    fun loadExpenses(monthYearVal: String) {
        val transactions = expensesRepository.loadExpenses(
            startDate = Hive().getDateFromString("01/$monthYearVal"),
            endDate = Hive().getDateFromString("31/$monthYearVal"))

        val sectionedTransactionsItem = mutableListOf<SectionedTransactionsEntity>()
        var previousTransaction = ""
        transactions.forEach {
            val currentTransaction = Hive().getStringFromDate(it.expenseDate)
            if (previousTransaction.isNullOrEmpty() || previousTransaction != currentTransaction) {
                previousTransaction = currentTransaction
                sectionedTransactionsItem.add(
                    SectionedTransactionsEntity.TransactionsHeader(
                        title = currentTransaction,
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
                ))
        }

        _uiState.postValue(DailyExpensesUIState.DailyExpenses(sectionedTransactionsItem))
    }
}

sealed class DailyExpensesActions {
    data class BottomNavigate(val bottomSheetDialogFragment: BottomSheetDialogFragment) : DailyExpensesActions()

    data class Navigate(val destination: NavDirections) : DailyExpensesActions()
}

sealed class DailyExpensesUIState {
    object Loading : DailyExpensesUIState()

    object Success : DailyExpensesUIState()

    data class DailyExpenses(val expensesEntity: List<SectionedTransactionsEntity>) : DailyExpensesUIState()

    data class Error(val statusCode: String, val message: String) : DailyExpensesUIState()
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
        var TotalExpense: String,
        var TotalIncome: String
    ) : SectionedTransactionsEntity()
}
