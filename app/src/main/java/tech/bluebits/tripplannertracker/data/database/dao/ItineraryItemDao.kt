package tech.bluebits.tripplannertracker.data.database.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import tech.bluebits.tripplannertracker.data.database.entity.ItineraryItemEntity
import tech.bluebits.tripplannertracker.data.model.ItineraryCategory
import java.time.LocalDate

@Dao
interface ItineraryItemDao {
    
    @Query("SELECT * FROM itinerary_items WHERE tripId = :tripId ORDER BY sortOrder ASC, date ASC, time ASC")
    fun getItineraryItemsByTripId(tripId: String): Flow<List<ItineraryItemEntity>>
    
    @Query("SELECT * FROM itinerary_items WHERE tripId = :tripId AND date = :date ORDER BY sortOrder ASC, time ASC")
    fun getItineraryItemsByTripIdAndDate(tripId: String, date: LocalDate): Flow<List<ItineraryItemEntity>>
    
    @Query("SELECT * FROM itinerary_items WHERE id = :id")
    suspend fun getItineraryItemById(id: String): ItineraryItemEntity?
    
    @Query("SELECT * FROM itinerary_items WHERE tripId = :tripId AND category = :category ORDER BY date ASC, time ASC")
    fun getItineraryItemsByCategory(tripId: String, category: ItineraryCategory): Flow<List<ItineraryItemEntity>>
    
    @Query("SELECT * FROM itinerary_items WHERE tripId = :tripId AND isCompleted = :isCompleted ORDER BY date ASC, time ASC")
    fun getItineraryItemsByCompletionStatus(tripId: String, isCompleted: Boolean): Flow<List<ItineraryItemEntity>>
    
    @Query("SELECT * FROM itinerary_items WHERE tripId = :tripId AND date >= :startDate AND date <= :endDate ORDER BY date ASC, time ASC")
    fun getItineraryItemsInDateRange(tripId: String, startDate: LocalDate, endDate: LocalDate): Flow<List<ItineraryItemEntity>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItineraryItem(itineraryItem: ItineraryItemEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItineraryItems(itineraryItems: List<ItineraryItemEntity>)
    
    @Update
    suspend fun updateItineraryItem(itineraryItem: ItineraryItemEntity)
    
    @Delete
    suspend fun deleteItineraryItem(itineraryItem: ItineraryItemEntity)
    
    @Query("DELETE FROM itinerary_items WHERE id = :id")
    suspend fun deleteItineraryItemById(id: String)
    
    @Query("DELETE FROM itinerary_items WHERE tripId = :tripId")
    suspend fun deleteItineraryItemsByTripId(tripId: String)
    
    @Query("UPDATE itinerary_items SET isCompleted = :isCompleted WHERE id = :id")
    suspend fun updateItineraryItemCompletion(id: String, isCompleted: Boolean)
    
    @Query("UPDATE itinerary_items SET sortOrder = :sortOrder WHERE id = :id")
    suspend fun updateItineraryItemSortOrder(id: String, sortOrder: Int)
    
    @Query("SELECT MAX(sortOrder) FROM itinerary_items WHERE tripId = :tripId")
    suspend fun getMaxSortOrder(tripId: String): Int?
}
