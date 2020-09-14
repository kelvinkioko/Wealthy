package com.money.budget.wealthy.ui.expenses.daily

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavDirections
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.money.budget.wealthy.database.models.ExpensesEntity
import com.money.budget.wealthy.database.repository.ExpensesRepository
import com.money.budget.wealthy.util.Event

class DailyExpensesViewModel(
    private val expensesRepository: ExpensesRepository
) : ViewModel() {

    private val _uiState = MutableLiveData<DailyExpensesUIState>()
    val uiState: LiveData<DailyExpensesUIState> = _uiState

    private val _action = MutableLiveData<Event<DailyExpensesActions>>()
    val action: LiveData<Event<DailyExpensesActions>> = _action

    init {
        loadExpenses()
    }

    fun loadExpenses() {
        val accounts = expensesRepository.loadExpenses()
        _uiState.postValue(DailyExpensesUIState.DailyExpenses(accounts))
    }
}

sealed class DailyExpensesActions {
    data class BottomNavigate(val bottomSheetDialogFragment: BottomSheetDialogFragment) : DailyExpensesActions()

    data class Navigate(val destination: NavDirections) : DailyExpensesActions()
}

sealed class DailyExpensesUIState {
    object Loading : DailyExpensesUIState()

    object Success : DailyExpensesUIState()

    data class DailyExpenses(val expensesEntity: List<ExpensesEntity>) : DailyExpensesUIState()

    data class Error(val statusCode: String, val message: String) : DailyExpensesUIState()
}
