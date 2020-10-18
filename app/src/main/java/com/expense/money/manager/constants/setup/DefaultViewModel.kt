package com.expense.money.manager.constants.setup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.expense.money.manager.constants.Hive
import com.expense.money.manager.constants.PreferenceHandler
import com.expense.money.manager.constants.StatusEnum
import com.expense.money.manager.database.models.CategoryTypesEntity
import com.expense.money.manager.database.models.CurrencyEntity
import com.expense.money.manager.database.models.TransactionTypesEntity
import com.expense.money.manager.database.repository.AccountsRepository
import com.expense.money.manager.database.repository.CategoryRepository
import com.expense.money.manager.database.repository.CurrencyRepository
import com.expense.money.manager.database.repository.TransactionRepository

class DefaultViewModel(
    private val accountsRepository: AccountsRepository,
    private val categoryRepository: CategoryRepository,
    private val currencyRepository: CurrencyRepository,
    private val transactionRepository: TransactionRepository
) : ViewModel() {

    private val _uiState = MutableLiveData<DefaultUIState>(DefaultUIState.Loading)
    val uiState: LiveData<DefaultUIState> = _uiState

    init {
        setupCurrency()
        setupTransactionTypes()
        setupExpenseCategories()
    }

    private fun setupCurrency() {
        val currency = mutableListOf<CurrencyEntity>()

        currency.add(CurrencyEntity(0, "Bulgaria", "Bulgarian lev", "BGN", "лв", "Europe"))
        currency.add(CurrencyEntity(0, "Switzerland", "Swiss franc", "CHF", "CHF", "Europe"))
        currency.add(CurrencyEntity(0, "Czechia", "Czech koruna", "CZK", "Kč", "Europe"))
        currency.add(CurrencyEntity(0, "Denmark", "Danish krone", "DKK", "kr", "Europe"))
        currency.add(CurrencyEntity(0, "Euro area countries", "Euro", "EUR", "€", "Europe"))
        currency.add(CurrencyEntity(0, "United Kingdom", "Pounds sterling", "GBP", "£", "Europe"))
        currency.add(CurrencyEntity(0, "Croatia", "Croatian Kuna", "HRK", "kn", "Europe"))
        currency.add(CurrencyEntity(0, "Georgia", "Georgian lari", "GEL", "₾", "Europe"))
        currency.add(CurrencyEntity(0, "Hungary", "Hungarian forint", "HUF", "ft", "Europe"))
        currency.add(CurrencyEntity(0, "Norway", "Norwegian krone", "NOK", "kr", "Europe"))
        currency.add(CurrencyEntity(0, "Poland", "Polish zloty", "PLN", "zł", "Europe"))
        currency.add(CurrencyEntity(0, "Russia", "Russian ruble", "RUB", "₽", "Europe"))
        currency.add(CurrencyEntity(0, "Romania", "Romanian leu", "RON", "lei", "Europe"))
        currency.add(CurrencyEntity(0, "Sweden", "Swedish krona", "SEK", "kr", "Europe"))
        currency.add(CurrencyEntity(0, "Turkey", "Turkish lira", "TRY", "₺", "Europe"))
        currency.add(CurrencyEntity(0, "Ukraine", "Ukrainian hryvna", "UAH", "₴", "Europe"))

        currency.add(CurrencyEntity(0, "UAE", "Emirati dirham", "AED", "د.إ", "Middle East and Africa"))
        currency.add(CurrencyEntity(0, "Israel", "Israeli shekel", "ILS", "₪", "Middle East and Africa"))
        currency.add(CurrencyEntity(0, "Kenya", "Kenyan shilling", "KES", "Ksh", "Middle East and Africa"))
        currency.add(CurrencyEntity(0, "Morocco", "Moroccan dirham", "MAD", ".د.م", "Middle East and Africa"))
        currency.add(CurrencyEntity(0, "Nigeria", "igerian naira", "NGN", "₦", "Middle East and Africa"))
        currency.add(CurrencyEntity(0, "South Africa", "South african rand**", "ZAR", "R", "Middle East and Africa"))

        currency.add(CurrencyEntity(0, "Brazil", "Brazilian real", "BRL", "R$", "The Americas"))
        currency.add(CurrencyEntity(0, "Canada", "Canadian dollars", "CAD", "$", "The Americas"))
        currency.add(CurrencyEntity(0, "Chile", "Chilean peso", "CLP", "$", "The Americas"))
        currency.add(CurrencyEntity(0, "Colombia", "Colombian peso", "COP", "$", "The Americas"))
        currency.add(CurrencyEntity(0, "Mexico", "Mexican peso", "MXN", "$", "The Americas"))
        currency.add(CurrencyEntity(0, "Peru", "Peruvian sol", "PEN", "S/.", "The Americas"))
        currency.add(CurrencyEntity(0, "USA", "US dollar", "USD", "$", "The Americas"))

        currency.add(CurrencyEntity(0, "Australia", "Australian dollars", "AUD", "$", "Asia & The Pacific region"))
        currency.add(CurrencyEntity(0, "Bangladesh", "Bangladeshi taka", "BDT", "৳", "Asia & The Pacific region"))
        currency.add(CurrencyEntity(0, "China", "Chinese yuan", "CNY", "¥", "Asia & The Pacific region"))
        currency.add(CurrencyEntity(0, "Hong Kong", "Hong Kong dollar", "HKD", "$", "Asia & The Pacific region"))
        currency.add(CurrencyEntity(0, "Indonesia", "Indonesian rupiah", "IDR", "Rp", "Asia & The Pacific region"))

        currency.add(CurrencyEntity(0, "India", "Indian rupee", "INR", "₹", "Asia & The Pacific region"))
        currency.add(CurrencyEntity(0, "Japan", "Japanese yen", "JPY", "¥", "Asia & The Pacific region"))
        currency.add(CurrencyEntity(0, "Malaysia", "Malaysian ringgit", "MYR", "RM", "Asia & The Pacific region"))
        currency.add(CurrencyEntity(0, "New Zealand", "New Zealand dollar", "NZD", "$", "Asia & The Pacific region"))
        currency.add(CurrencyEntity(0, "Philippines", "Philippine peso", "PHP", "₱", "Asia & The Pacific region"))

        currency.add(CurrencyEntity(0, "Pakistan", "Pakistani rupee", "PKR", "Rs", "Asia & The Pacific region"))
        currency.add(CurrencyEntity(0, "Singapore", "Singapore dollar", "SGD", "$", "Asia & The Pacific region"))
        currency.add(CurrencyEntity(0, "South Korea", "South Korean won", "KRW", "₩", "Asia & The Pacific region"))
        currency.add(CurrencyEntity(0, "Sri Lanka", "Sri Lankan rupee", "LKR", "Rs", "Asia & The Pacific region"))
        currency.add(CurrencyEntity(0, "Thailand", "Thai baht", "THB", "฿", "Asia & The Pacific region"))
        currency.add(CurrencyEntity(0, "Vietnam", "Vietnamese dong", "VND", "₫", "Asia & The Pacific region"))

        if (currency.size != currencyRepository.countCurrency()) {
            currencyRepository.deleteCurrency()
            for (singleCurrency in currency) {
                currencyRepository.insertCurrency(singleCurrency)
            }
        }
    }

    private fun setupTransactionTypes() {
        val transactionType = mutableListOf<TransactionTypesEntity>()

        transactionType.add(TransactionTypesEntity(
            id = 0,
            transactionID = "TTE-20200918-103320",
            transactionName = "Income",
            transactionDescription = "Money coming into you account, wallet or other personal sources",
            createdAt = Hive().getCurrentDateTime()
        ))

        transactionType.add(TransactionTypesEntity(
            id = 0,
            transactionID = "TTE-20200918-103430",
            transactionName = "Expense",
            transactionDescription = "Money spent from your account",
            createdAt = Hive().getCurrentDateTime()
        ))

        transactionType.add(TransactionTypesEntity(
            id = 0,
            transactionID = "TTE-20200918-103540",
            transactionName = "Transfer",
            transactionDescription = "Money moved from one account to another",
            createdAt = Hive().getCurrentDateTime()
        ))

        if (transactionType.size != transactionRepository.countTransactionTypes()) {
            transactionRepository.deleteTransactionTypes()
            for (singleTransactionType in transactionType) {
                transactionRepository.insertTransactionTypes(singleTransactionType)
            }
        }
    }

    private fun setupExpenseCategories() {
        val categoryTypes = mutableListOf<CategoryTypesEntity>()

        categoryTypes.add(CategoryTypesEntity(
            id = 0,
            categoryID = "CAT-${Hive().getTimestamp()}",
            categoryName = "Salary",
            categoryDescription = "Regular income from formal employment.",
            transactionType = "Income#TTE-20200918-103320",
            categoryStatus = StatusEnum.ACTIVE,
            createdAt = Hive().getCurrentDateTime()
        ))

        categoryTypes.add(CategoryTypesEntity(
            id = 0,
            categoryID = "CAT-${Hive().getTimestamp()}",
            categoryName = "Business",
            categoryDescription = "Financed from partially or fully owned business",
            transactionType = "Income#TTE-20200918-103320",
            categoryStatus = StatusEnum.ACTIVE,
            createdAt = Hive().getCurrentDateTime()
        ))

        categoryTypes.add(CategoryTypesEntity(
            id = 0,
            categoryID = "CAT-${Hive().getTimestamp()}",
            categoryName = "Loan",
            categoryDescription = "Sum of money borrowed and is expected to be paid back with interest",
            transactionType = "Income#TTE-20200918-103320",
            categoryStatus = StatusEnum.ACTIVE,
            createdAt = Hive().getCurrentDateTime()
        ))

        categoryTypes.add(CategoryTypesEntity(
            id = 0,
            categoryID = "CAT-${Hive().getTimestamp()}",
            categoryName = "Debt repayment",
            categoryDescription = "past lending repaid",
            transactionType = "Income#TTE-20200918-103320",
            categoryStatus = StatusEnum.ACTIVE,
            createdAt = Hive().getCurrentDateTime()
        ))

        categoryTypes.add(CategoryTypesEntity(
            id = 0,
            categoryID = "CAT-${Hive().getTimestamp()}",
            categoryName = "Food",
            categoryDescription = "Food",
            transactionType = "Expense#TTE-20200918-103430",
            categoryStatus = StatusEnum.ACTIVE,
            createdAt = Hive().getCurrentDateTime()
        ))

        categoryTypes.add(CategoryTypesEntity(
            id = 0,
            categoryID = "CAT-${Hive().getTimestamp()}",
            categoryName = "Social life",
            categoryDescription = "Social",
            transactionType = "Expense#TTE-20200918-103430",
            categoryStatus = StatusEnum.ACTIVE,
            createdAt = Hive().getCurrentDateTime()
        ))

        categoryTypes.add(CategoryTypesEntity(
            id = 0,
            categoryID = "CAT-${Hive().getTimestamp()}",
            categoryName = "Transportation",
            categoryDescription = "Movement from one destination to another",
            transactionType = "Expense#TTE-20200918-103430",
            categoryStatus = StatusEnum.ACTIVE,
            createdAt = Hive().getCurrentDateTime()
        ))

        categoryTypes.add(CategoryTypesEntity(
            id = 0,
            categoryID = "CAT-${Hive().getTimestamp()}",
            categoryName = "Education",
            categoryDescription = "education",
            transactionType = "Expense#TTE-20200918-103430",
            categoryStatus = StatusEnum.ACTIVE,
            createdAt = Hive().getCurrentDateTime()
        ))

        if (categoryTypes.size != categoryRepository.countCategoryTypes()) {
            categoryRepository.deleteCategoryTypes()
            for (singleCategoryType in categoryTypes) {
                categoryRepository.insertCategoryTypes(singleCategoryType)
            }
        }
    }

    fun validateSetup(preferenceHandler: PreferenceHandler): Boolean {
        val accountTypesSetup = (preferenceHandler.getAccountTypesSetup()!! && (accountsRepository.countAccountTypes() > 0))
        val accountSetup = (preferenceHandler.getAccountSetup()!! && (accountsRepository.countAccounts() > 0))
        val currencySetup = preferenceHandler.getCurrencySetup()!!
        val expenseSetup = preferenceHandler.getExpensesSetup()!!
        return (currencySetup && accountTypesSetup && accountSetup && expenseSetup)
    }
}

sealed class DefaultUIState {
    object Loading : DefaultUIState()

    object Success : DefaultUIState()
}
