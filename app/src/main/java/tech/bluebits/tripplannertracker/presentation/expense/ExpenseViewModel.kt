package tech.bluebits.tripplannertracker.presentation.expense

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import tech.bluebits.tripplannertracker.data.model.Expense
import tech.bluebits.tripplannertracker.data.model.ExpenseCategory
import tech.bluebits.tripplannertracker.data.repository.ExpenseRepository
import tech.bluebits.tripplannertracker.data.repository.TripRepository
import tech.bluebits.tripplannertracker.presentation.base.BaseViewModel
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ExpenseViewModel @Inject constructor(
    private val expenseRepository: ExpenseRepository,
    private val tripRepository: TripRepository
) : BaseViewModel<ExpenseState, ExpenseIntent, ExpenseEffect>(ExpenseState()) {

    override fun handleIntent(intent: ExpenseIntent) {
        when (intent) {
            is ExpenseIntent.LoadExpenses -> loadExpenses()
            is ExpenseIntent.LoadExpensesForCategory -> loadExpensesForCategory(intent.category)
            is ExpenseIntent.LoadExpensesForDate -> loadExpensesForDate(intent.date)
            is ExpenseIntent.AddExpense -> addExpense(intent.expense)
            is ExpenseIntent.UpdateExpense -> updateExpense(intent.expense)
            is ExpenseIntent.DeleteExpense -> deleteExpense(intent.expenseId)
            is ExpenseIntent.UpdateBudget -> updateBudget(intent.budget)
            is ExpenseIntent.UpdateCurrency -> updateCurrency(intent.currency)
            is ExpenseIntent.FilterByCategory -> filterByCategory(intent.category)
            is ExpenseIntent.FilterByDate -> filterByDate(intent.date)
            is ExpenseIntent.NavigateToAddExpense -> navigateToAddExpense()
            is ExpenseIntent.NavigateToEditExpense -> navigateToEditExpense(intent.expenseId)
            is ExpenseIntent.NavigateToBudgetSettings -> navigateToBudgetSettings()
            is ExpenseIntent.ExportExpenses -> exportExpenses()
        }
    }

    private fun loadExpenses() {
        if (state.value.tripId.isEmpty()) return
        
        viewModelScope.launch {
            updateState { copy(isLoading = true, error = null) }
            
            try {
                // Load trip info first
                val trip = tripRepository.getTripById(state.value.tripId)
                
                // Load expenses
                expenseRepository.getExpensesByTripId(state.value.tripId)
                    .catch { e ->
                        updateState {
                            copy(
                                isLoading = false,
                                error = e.message ?: "Failed to load expenses"
                            )
                        }
                        sendEffect(ExpenseEffect.ShowError(e.message ?: "Failed to load expenses"))
                    }
                    .collect { expenses ->
                        val totalSpent = expenseRepository.getTotalExpensesForTrip(state.value.tripId) ?: 0.0
                        val categoryExpenses = getCategoryExpenses()
                        val dailyExpenses = getDailyExpenses()
                        
                        updateState {
                            copy(
                                isLoading = false,
                                expenses = expenses,
                                totalBudget = trip?.totalBudget ?: 0.0,
                                totalSpent = totalSpent,
                                remainingBudget = (trip?.totalBudget ?: 0.0) - totalSpent,
                                currency = trip?.currency ?: "USD",
                                categoryExpenses = categoryExpenses,
                                dailyExpenses = dailyExpenses
                            )
                        }
                    }
            } catch (e: Exception) {
                updateState {
                    copy(
                        isLoading = false,
                        error = e.message ?: "Failed to load expenses"
                    )
                }
                sendEffect(ExpenseEffect.ShowError(e.message ?: "Failed to load expenses"))
            }
        }
    }

    private fun loadExpensesForCategory(category: ExpenseCategory) {
        if (state.value.tripId.isEmpty()) return
        
        viewModelScope.launch {
            updateState { copy(isLoading = true, selectedCategory = category, error = null) }
            
            try {
                expenseRepository.getExpensesByCategory(state.value.tripId, category)
                    .catch { e ->
                        updateState {
                            copy(
                                isLoading = false,
                                error = e.message ?: "Failed to load expenses for category"
                            )
                        }
                        sendEffect(ExpenseEffect.ShowError(e.message ?: "Failed to load expenses for category"))
                    }
                    .collect { expenses ->
                        updateState {
                            copy(
                                isLoading = false,
                                expenses = expenses
                            )
                        }
                    }
            } catch (e: Exception) {
                updateState {
                    copy(
                        isLoading = false,
                        error = e.message ?: "Failed to load expenses for category"
                    )
                }
                sendEffect(ExpenseEffect.ShowError(e.message ?: "Failed to load expenses for category"))
            }
        }
    }

    private fun loadExpensesForDate(date: LocalDate) {
        if (state.value.tripId.isEmpty()) return
        
        viewModelScope.launch {
            updateState { copy(isLoading = true, selectedDate = date, error = null) }
            
            try {
                expenseRepository.getExpensesInDateRange(state.value.tripId, date, date)
                    .catch { e ->
                        updateState {
                            copy(
                                isLoading = false,
                                error = e.message ?: "Failed to load expenses for date"
                            )
                        }
                        sendEffect(ExpenseEffect.ShowError(e.message ?: "Failed to load expenses for date"))
                    }
                    .collect { expenses ->
                        updateState {
                            copy(
                                isLoading = false,
                                expenses = expenses
                            )
                        }
                    }
            } catch (e: Exception) {
                updateState {
                    copy(
                        isLoading = false,
                        error = e.message ?: "Failed to load expenses for date"
                    )
                }
                sendEffect(ExpenseEffect.ShowError(e.message ?: "Failed to load expenses for date"))
            }
        }
    }

    private fun addExpense(expense: Expense) {
        viewModelScope.launch {
            try {
                val newExpense = expense.copy(
                    id = UUID.randomUUID().toString(),
                    createdAt = LocalDateTime.now(),
                    updatedAt = LocalDateTime.now()
                )
                expenseRepository.insertExpense(newExpense)
                sendEffect(ExpenseEffect.ShowExpenseAdded)
                loadExpenses() // Refresh the list
            } catch (e: Exception) {
                sendEffect(ExpenseEffect.ShowError(e.message ?: "Failed to add expense"))
            }
        }
    }

    private fun updateExpense(expense: Expense) {
        viewModelScope.launch {
            try {
                val updatedExpense = expense.copy(
                    updatedAt = LocalDateTime.now()
                )
                expenseRepository.updateExpense(updatedExpense)
                sendEffect(ExpenseEffect.ShowExpenseUpdated)
                loadExpenses() // Refresh the list
            } catch (e: Exception) {
                sendEffect(ExpenseEffect.ShowError(e.message ?: "Failed to update expense"))
            }
        }
    }

    private fun deleteExpense(expenseId: String) {
        viewModelScope.launch {
            try {
                expenseRepository.deleteExpenseById(expenseId)
                sendEffect(ExpenseEffect.ShowExpenseDeleted)
                loadExpenses() // Refresh the list
            } catch (e: Exception) {
                sendEffect(ExpenseEffect.ShowError(e.message ?: "Failed to delete expense"))
            }
        }
    }

    private fun updateBudget(budget: Double) {
        viewModelScope.launch {
            try {
                val trip = tripRepository.getTripById(state.value.tripId)
                if (trip != null) {
                    val updatedTrip = trip.copy(
                        totalBudget = budget,
                        updatedAt = LocalDateTime.now()
                    )
                    tripRepository.updateTrip(updatedTrip)
                    sendEffect(ExpenseEffect.ShowBudgetUpdated)
                    loadExpenses() // Refresh the list
                }
            } catch (e: Exception) {
                sendEffect(ExpenseEffect.ShowError(e.message ?: "Failed to update budget"))
            }
        }
    }

    private fun updateCurrency(currency: String) {
        viewModelScope.launch {
            try {
                val trip = tripRepository.getTripById(state.value.tripId)
                if (trip != null) {
                    val updatedTrip = trip.copy(
                        currency = currency,
                        updatedAt = LocalDateTime.now()
                    )
                    tripRepository.updateTrip(updatedTrip)
                    loadExpenses() // Refresh the list
                }
            } catch (e: Exception) {
                sendEffect(ExpenseEffect.ShowError(e.message ?: "Failed to update currency"))
            }
        }
    }

    private fun filterByCategory(category: ExpenseCategory?) {
        updateState { copy(selectedCategory = category) }
        if (category != null) {
            loadExpensesForCategory(category)
        } else {
            loadExpenses()
        }
    }

    private fun filterByDate(date: LocalDate?) {
        updateState { copy(selectedDate = date) }
        if (date != null) {
            loadExpensesForDate(date)
        } else {
            loadExpenses()
        }
    }

    private fun navigateToAddExpense() {
        sendEffect(ExpenseEffect.NavigateToAddExpense)
    }

    private fun navigateToEditExpense(expenseId: String) {
        sendEffect(ExpenseEffect.NavigateToEditExpense(expenseId))
    }

    private fun navigateToBudgetSettings() {
        sendEffect(ExpenseEffect.NavigateToBudgetSettings)
    }

    private fun exportExpenses() {
        viewModelScope.launch {
            try {
                // TODO: Implement CSV export functionality
                val filePath = "expenses_${state.value.tripId}_${System.currentTimeMillis()}.csv"
                sendEffect(ExpenseEffect.ShowExportSuccess(filePath))
            } catch (e: Exception) {
                sendEffect(ExpenseEffect.ShowError(e.message ?: "Failed to export expenses"))
            }
        }
    }

    private suspend fun getCategoryExpenses(): Map<ExpenseCategory, Double> {
        return try {
            expenseRepository.getExpenseSummaryByCategory(state.value.tripId)
                .associate { it.category to it.total }
        } catch (e: Exception) {
            emptyMap()
        }
    }

    private suspend fun getDailyExpenses(): Map<LocalDate, Double> {
        return try {
            expenseRepository.getDailyExpenseSummary(state.value.tripId)
                .associate { it.date to it.dailyTotal }
        } catch (e: Exception) {
            emptyMap()
        }
    }

    fun setTripId(tripId: String) {
        updateState { copy(tripId = tripId) }
        loadExpenses()
    }
}
