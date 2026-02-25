package tech.bluebits.tripplannertracker.presentation.expense

import tech.bluebits.tripplannertracker.data.model.Expense
import tech.bluebits.tripplannertracker.data.model.ExpenseCategory
import tech.bluebits.tripplannertracker.presentation.base.UiState
import tech.bluebits.tripplannertracker.presentation.base.UiIntent
import tech.bluebits.tripplannertracker.presentation.base.UiEffect
import java.time.LocalDate

data class ExpenseState(
    val isLoading: Boolean = false,
    val tripId: String = "",
    val expenses: List<Expense> = emptyList(),
    val totalBudget: Double = 0.0,
    val totalSpent: Double = 0.0,
    val remainingBudget: Double = 0.0,
    val currency: String = "USD",
    val categoryExpenses: Map<ExpenseCategory, Double> = emptyMap(),
    val dailyExpenses: Map<LocalDate, Double> = emptyMap(),
    val selectedCategory: ExpenseCategory? = null,
    val selectedDate: LocalDate? = null,
    val error: String? = null
) : UiState

sealed class ExpenseIntent : UiIntent {
    object LoadExpenses : ExpenseIntent()
    data class LoadExpensesForCategory(val category: ExpenseCategory) : ExpenseIntent()
    data class LoadExpensesForDate(val date: LocalDate) : ExpenseIntent()
    data class AddExpense(val expense: Expense) : ExpenseIntent()
    data class UpdateExpense(val expense: Expense) : ExpenseIntent()
    data class DeleteExpense(val expenseId: String) : ExpenseIntent()
    data class UpdateBudget(val budget: Double) : ExpenseIntent()
    data class UpdateCurrency(val currency: String) : ExpenseIntent()
    data class FilterByCategory(val category: ExpenseCategory?) : ExpenseIntent()
    data class FilterByDate(val date: LocalDate?) : ExpenseIntent()
    data class NavigateToAddExpense : ExpenseIntent()
    data class NavigateToEditExpense(val expenseId: String) : ExpenseIntent()
    object NavigateToBudgetSettings : ExpenseIntent()
    object ExportExpenses : ExpenseIntent()
}

sealed class ExpenseEffect : UiEffect {
    data class ShowError(val message: String) : ExpenseEffect()
    object NavigateToAddExpense : ExpenseEffect()
    data class NavigateToEditExpense(val expenseId: String) : ExpenseEffect()
    object NavigateToBudgetSettings : ExpenseEffect()
    object ShowExpenseAdded : ExpenseEffect()
    object ShowExpenseUpdated : ExpenseEffect()
    object ShowExpenseDeleted : ExpenseEffect()
    object ShowBudgetUpdated : ExpenseEffect()
    data class ShowExportSuccess(val filePath: String) : ExpenseEffect()
}
