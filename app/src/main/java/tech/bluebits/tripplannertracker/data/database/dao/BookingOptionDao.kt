package tech.bluebits.tripplannertracker.data.database.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import tech.bluebits.tripplannertracker.data.database.entity.BookingOptionEntity

@Dao
interface BookingOptionDao {
    
    @Query("SELECT * FROM booking_options WHERE tripId = :tripId ORDER BY createdAt DESC")
    fun getBookingOptionsByTripId(tripId: String): Flow<List<BookingOptionEntity>>
    
    @Query("SELECT * FROM booking_options WHERE id = :id")
    suspend fun getBookingOptionById(id: String): BookingOptionEntity?
    
    @Query("SELECT * FROM booking_options WHERE tripId = :tripId AND type = :type ORDER BY createdAt DESC")
    fun getBookingOptionsByType(tripId: String, type: String): Flow<List<BookingOptionEntity>>
    
    @Query("SELECT * FROM booking_options WHERE tripId = :tripId AND isSelected = :isSelected ORDER BY createdAt DESC")
    fun getBookingOptionsBySelectionStatus(tripId: String, isSelected: Boolean): Flow<List<BookingOptionEntity>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookingOption(bookingOption: BookingOptionEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookingOptions(bookingOptions: List<BookingOptionEntity>)
    
    @Update
    suspend fun updateBookingOption(bookingOption: BookingOptionEntity)
    
    @Delete
    suspend fun deleteBookingOption(bookingOption: BookingOptionEntity)
    
    @Query("DELETE FROM booking_options WHERE id = :id")
    suspend fun deleteBookingOptionById(id: String)
    
    @Query("DELETE FROM booking_options WHERE tripId = :tripId")
    suspend fun deleteBookingOptionsByTripId(tripId: String)
    
    @Query("UPDATE booking_options SET isSelected = :isSelected WHERE id = :id")
    suspend fun updateBookingOptionSelection(id: String, isSelected: Boolean)
    
    @Query("UPDATE booking_options SET isSelected = 0 WHERE tripId = :tripId AND type = :type")
    suspend fun clearSelectionForType(tripId: String, type: String)
}
