package tech.bluebits.tripplannertracker.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import tech.bluebits.tripplannertracker.data.database.dao.TripDao
import tech.bluebits.tripplannertracker.data.database.entity.TripEntity
import tech.bluebits.tripplannertracker.data.model.Trip
import tech.bluebits.tripplannertracker.data.model.TripStatus
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TripRepository @Inject constructor(
    private val tripDao: TripDao
) {
    
    fun getAllTrips(): Flow<List<Trip>> {
        return tripDao.getAllTrips().map { entities ->
            entities.map { it.toTrip() }
        }
    }
    
    fun getTripsByStatus(status: TripStatus): Flow<List<Trip>> {
        return tripDao.getTripsByStatus(status).map { entities ->
            entities.map { it.toTrip() }
        }
    }
    
    suspend fun getTripById(id: String): Trip? {
        return tripDao.getTripById(id)?.toTrip()
    }
    
    suspend fun getActiveTrip(): Trip? {
        return tripDao.getActiveTrip()?.toTrip()
    }
    
    fun getUpcomingTrips(limit: Int = 5): Flow<List<Trip>> {
        return tripDao.getUpcomingTrips(limit).map { entities ->
            entities.map { it.toTrip() }
        }
    }
    
    fun getPastTrips(limit: Int = 5): Flow<List<Trip>> {
        return tripDao.getPastTrips(limit).map { entities ->
            entities.map { it.toTrip() }
        }
    }
    
    suspend fun insertTrip(trip: Trip) {
        tripDao.insertTrip(trip.toEntity())
    }
    
    suspend fun updateTrip(trip: Trip) {
        tripDao.updateTrip(trip.toEntity())
    }
    
    suspend fun deleteTrip(trip: Trip) {
        tripDao.deleteTrip(trip.toEntity())
    }
    
    suspend fun deleteTripById(id: String) {
        tripDao.deleteTripById(id)
    }
    
    suspend fun updateTripStatus(id: String, status: TripStatus) {
        tripDao.updateTripStatus(id, status)
    }
}

// Extension functions for mapping between Entity and Model
fun TripEntity.toTrip(): Trip {
    return Trip(
        id = id,
        name = name,
        destination = destination,
        startDate = startDate,
        endDate = endDate,
        coverImageUrl = coverImageUrl,
        notes = notes,
        status = status,
        createdAt = createdAt,
        updatedAt = updatedAt,
        totalBudget = totalBudget,
        currency = currency
    )
}

fun Trip.toEntity(): TripEntity {
    return TripEntity(
        id = id,
        name = name,
        destination = destination,
        startDate = startDate,
        endDate = endDate,
        coverImageUrl = coverImageUrl,
        notes = notes,
        status = status,
        createdAt = createdAt,
        updatedAt = updatedAt,
        totalBudget = totalBudget,
        currency = currency
    )
}
