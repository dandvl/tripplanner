package tech.bluebits.tripplannertracker.data.database.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import tech.bluebits.tripplannertracker.data.database.entity.ExpenseEntity
import tech.bluebits.tripplannertracker.data.model.ExpenseCategory
import java.time.LocalDate

@Dao
interface ExpenseDao {
    
    @Query("SELECT * FROM expenses WHERE tripId = :tripId ORDER BY date DESC")
    fun getExpensesByTripId(tripId: String): Flow<List<ExpenseEntity>>
    
    @Query("SELECT * FROM expenses WHERE id = :id")
    suspend fun getExpenseById(id: String): ExpenseEntity?
    
    @Query("SELECT * FROM expenses WHERE tripId = :tripId AND category = :category ORDER BY date DESC")
    fun getExpensesByCategory(tripId: String, category: ExpenseCategory): Flow<List<ExpenseEntity>>
    
    @Query("SELECT * FROM expenses WHERE tripId = :tripId AND date >= :startDate AND date <= :endDate ORDER BY date DESC")
    fun getExpensesInDateRange(tripId: String, startDate: LocalDate, endDate: LocalDate): Flow<List<ExpenseEntity>>
    
    @Query("SELECT SUM(amount) FROM expenses WHERE tripId = :tripId")
    suspend fun getTotalExpensesForTrip(tripId: String): Double?
    
    @Query("SELECT SUM(amount) FROM expenses WHERE tripId = :tripId AND category = :category")
    suspend fun getExpensesByCategoryForTrip(tripId: String, category: ExpenseCategory): Double?
    
    @Query("SELECT category, SUM(amount) as total FROM expenses WHERE tripId = :tripId GROUP BY category")
    suspend fun getExpenseSummaryByCategory(tripId: String): List<CategoryExpenseSummary>
    
    @Query("SELECT date, SUM(amount) as dailyTotal FROM expenses WHERE tripId = :tripId GROUP BY date ORDER BY date DESC")
    suspend fun getDailyExpenseSummary(tripId: String): List<DailyExpenseSummary>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpense(expense: ExpenseEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpenses(expenses: List<ExpenseEntity>)
    
    @Update
    suspend fun updateExpense(expense: ExpenseEntity)
    
    @Delete
    suspend fun deleteExpense(expense: ExpenseEntity)
    
    @Query("DELETE FROM expenses WHERE id = :id")
    suspend fun deleteExpenseById(id: String)
    
    @Query("DELETE FROM expenses WHERE tripId = :tripId")
    suspend fun deleteExpensesByTripId(tripId: String)
}

data class CategoryExpenseSummary(
    val category: ExpenseCategory,
    val total: Double
)

data class DailyExpenseSummary(
    val date: LocalDate,
    val dailyTotal: Double
)
