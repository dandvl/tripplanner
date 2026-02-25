package tech.bluebits.tripplannertracker.data.database.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import tech.bluebits.tripplannertracker.data.database.entity.TripEntity
import tech.bluebits.tripplannertracker.data.model.TripStatus
import java.time.LocalDate

@Dao
interface TripDao {
    
    @Query("SELECT * FROM trips ORDER BY startDate ASC")
    fun getAllTrips(): Flow<List<TripEntity>>
    
    @Query("SELECT * FROM trips WHERE status = :status ORDER BY startDate ASC")
    fun getTripsByStatus(status: TripStatus): Flow<List<TripEntity>>
    
    @Query("SELECT * FROM trips WHERE id = :id")
    suspend fun getTripById(id: String): TripEntity?
    
    @Query("SELECT * FROM trips WHERE date('startDate') <= date('now') AND date('endDate') >= date('now') LIMIT 1")
    suspend fun getActiveTrip(): TripEntity?
    
    @Query("SELECT * FROM trips WHERE date('startDate') > date('now') ORDER BY startDate ASC LIMIT :limit")
    fun getUpcomingTrips(limit: Int = 5): Flow<List<TripEntity>>
    
    @Query("SELECT * FROM trips WHERE date('endDate') < date('now') ORDER BY startDate DESC LIMIT :limit")
    fun getPastTrips(limit: Int = 5): Flow<List<TripEntity>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrip(trip: TripEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrips(trips: List<TripEntity>)
    
    @Update
    suspend fun updateTrip(trip: TripEntity)
    
    @Delete
    suspend fun deleteTrip(trip: TripEntity)
    
    @Query("DELETE FROM trips WHERE id = :id")
    suspend fun deleteTripById(id: String)
    
    @Query("UPDATE trips SET status = :status WHERE id = :id")
    suspend fun updateTripStatus(id: String, status: TripStatus)
}
