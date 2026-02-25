package tech.bluebits.tripplannertracker.data.database.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import tech.bluebits.tripplannertracker.data.database.entity.VisitedLocationEntity
import java.time.LocalDateTime

@Dao
interface VisitedLocationDao {
    
    @Query("SELECT * FROM visited_locations WHERE tripId = :tripId ORDER BY visitedAt DESC")
    fun getVisitedLocationsByTripId(tripId: String): Flow<List<VisitedLocationEntity>>
    
    @Query("SELECT * FROM visited_locations WHERE id = :id")
    suspend fun getVisitedLocationById(id: String): VisitedLocationEntity?
    
    @Query("SELECT * FROM visited_locations WHERE tripId = :tripId AND isManual = :isManual ORDER BY visitedAt DESC")
    fun getVisitedLocationsByManualStatus(tripId: String, isManual: Boolean): Flow<List<VisitedLocationEntity>>
    
    @Query("SELECT * FROM visited_locations WHERE tripId = :tripId AND visitedAt >= :startTime AND visitedAt <= :endTime ORDER BY visitedAt DESC")
    fun getVisitedLocationsInTimeRange(tripId: String, startTime: LocalDateTime, endTime: LocalDateTime): Flow<List<VisitedLocationEntity>>
    
    @Query("SELECT COUNT(*) FROM visited_locations WHERE tripId = :tripId")
    suspend fun getVisitedLocationCount(tripId: String): Int
    
    @Query("SELECT DISTINCT latitude, longitude FROM visited_locations WHERE tripId = :tripId")
    suspend fun getUniqueCoordinatesForTrip(tripId: String): List<CoordinatePoint>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVisitedLocation(visitedLocation: VisitedLocationEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVisitedLocations(visitedLocations: List<VisitedLocationEntity>)
    
    @Update
    suspend fun updateVisitedLocation(visitedLocation: VisitedLocationEntity)
    
    @Delete
    suspend fun deleteVisitedLocation(visitedLocation: VisitedLocationEntity)
    
    @Query("DELETE FROM visited_locations WHERE id = :id")
    suspend fun deleteVisitedLocationById(id: String)
    
    @Query("DELETE FROM visited_locations WHERE tripId = :tripId")
    suspend fun deleteVisitedLocationsByTripId(tripId: String)
    
    @Query("DELETE FROM visited_locations WHERE tripId = :tripId AND isManual = false")
    suspend fun deleteAutoTrackedLocations(tripId: String)
}

data class CoordinatePoint(
    val latitude: Double,
    val longitude: Double
)
