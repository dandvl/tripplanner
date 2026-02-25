package tech.bluebits.tripplannertracker.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import tech.bluebits.tripplannertracker.data.database.dao.ExpenseDao
import tech.bluebits.tripplannertracker.data.database.entity.ExpenseEntity
import tech.bluebits.tripplannertracker.data.model.Expense
import tech.bluebits.tripplannertracker.data.model.ExpenseCategory
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExpenseRepository @Inject constructor(
    private val expenseDao: ExpenseDao
) {
    
    fun getExpensesByTripId(tripId: String): Flow<List<Expense>> {
        return expenseDao.getExpensesByTripId(tripId).map { entities ->
            entities.map { it.toExpense() }
        }
    }
    
    suspend fun getExpenseById(id: String): Expense? {
        return expenseDao.getExpenseById(id)?.toExpense()
    }
    
    fun getExpensesByCategory(tripId: String, category: ExpenseCategory): Flow<List<Expense>> {
        return expenseDao.getExpensesByCategory(tripId, category).map { entities ->
            entities.map { it.toExpense() }
        }
    }
    
    fun getExpensesInDateRange(tripId: String, startDate: LocalDate, endDate: LocalDate): Flow<List<Expense>> {
        return expenseDao.getExpensesInDateRange(tripId, startDate, endDate).map { entities ->
            entities.map { it.toExpense() }
        }
    }
    
    suspend fun getTotalExpensesForTrip(tripId: String): Double? {
        return expenseDao.getTotalExpensesForTrip(tripId)
    }
    
    suspend fun getExpensesByCategoryForTrip(tripId: String, category: ExpenseCategory): Double? {
        return expenseDao.getExpensesByCategoryForTrip(tripId, category)
    }
    
    suspend fun getExpenseSummaryByCategory(tripId: String): List<tech.bluebits.tripplannertracker.data.database.dao.CategoryExpenseSummary> {
        return expenseDao.getExpenseSummaryByCategory(tripId)
    }
    
    suspend fun getDailyExpenseSummary(tripId: String): List<tech.bluebits.tripplannertracker.data.database.dao.DailyExpenseSummary> {
        return expenseDao.getDailyExpenseSummary(tripId)
    }
    
    suspend fun insertExpense(expense: Expense) {
        expenseDao.insertExpense(expense.toEntity())
    }
    
    suspend fun insertExpenses(expenses: List<Expense>) {
        expenseDao.insertExpenses(expenses.map { it.toEntity() })
    }
    
    suspend fun updateExpense(expense: Expense) {
        expenseDao.updateExpense(expense.toEntity())
    }
    
    suspend fun deleteExpense(expense: Expense) {
        expenseDao.deleteExpense(expense.toEntity())
    }
    
    suspend fun deleteExpenseById(id: String) {
        expenseDao.deleteExpenseById(id)
    }
    
    suspend fun deleteExpensesByTripId(tripId: String) {
        expenseDao.deleteExpensesByTripId(tripId)
    }
}

fun ExpenseEntity.toExpense(): Expense {
    return Expense(
        id = id,
        tripId = tripId,
        title = title,
        category = category,
        amount = amount,
        currency = currency,
        date = date,
        receiptImageUrl = receiptImageUrl,
        latitude = latitude,
        longitude = longitude,
        notes = notes,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun Expense.toEntity(): ExpenseEntity {
    return ExpenseEntity(
        id = id,
        tripId = tripId,
        title = title,
        category = category,
        amount = amount,
        currency = currency,
        date = date,
        receiptImageUrl = receiptImageUrl,
        latitude = latitude,
        longitude = longitude,
        notes = notes,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}
