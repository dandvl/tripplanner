package tech.bluebits.tripplannertracker.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import tech.bluebits.tripplannertracker.data.database.dao.VisitedLocationDao
import tech.bluebits.tripplannertracker.data.database.entity.VisitedLocationEntity
import tech.bluebits.tripplannertracker.data.model.VisitedLocation
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VisitedLocationRepository @Inject constructor(
    private val visitedLocationDao: VisitedLocationDao
) {
    
    fun getVisitedLocationsByTripId(tripId: String): Flow<List<VisitedLocation>> {
        return visitedLocationDao.getVisitedLocationsByTripId(tripId).map { entities ->
            entities.map { it.toVisitedLocation() }
        }
    }
    
    suspend fun getVisitedLocationById(id: String): VisitedLocation? {
        return visitedLocationDao.getVisitedLocationById(id)?.toVisitedLocation()
    }
    
    fun getVisitedLocationsByManualStatus(tripId: String, isManual: Boolean): Flow<List<VisitedLocation>> {
        return visitedLocationDao.getVisitedLocationsByManualStatus(tripId, isManual).map { entities ->
            entities.map { it.toVisitedLocation() }
        }
    }
    
    fun getVisitedLocationsInTimeRange(tripId: String, startTime: LocalDateTime, endTime: LocalDateTime): Flow<List<VisitedLocation>> {
        return visitedLocationDao.getVisitedLocationsInTimeRange(tripId, startTime, endTime).map { entities ->
            entities.map { it.toVisitedLocation() }
        }
    }
    
    suspend fun getVisitedLocationCount(tripId: String): Int {
        return visitedLocationDao.getVisitedLocationCount(tripId)
    }
    
    suspend fun getUniqueCoordinatesForTrip(tripId: String): List<tech.bluebits.tripplannertracker.data.database.dao.CoordinatePoint> {
        return visitedLocationDao.getUniqueCoordinatesForTrip(tripId)
    }
    
    suspend fun insertVisitedLocation(visitedLocation: VisitedLocation) {
        visitedLocationDao.insertVisitedLocation(visitedLocation.toEntity())
    }
    
    suspend fun insertVisitedLocations(visitedLocations: List<VisitedLocation>) {
        visitedLocationDao.insertVisitedLocations(visitedLocations.map { it.toEntity() })
    }
    
    suspend fun updateVisitedLocation(visitedLocation: VisitedLocation) {
        visitedLocationDao.updateVisitedLocation(visitedLocation.toEntity())
    }
    
    suspend fun deleteVisitedLocation(visitedLocation: VisitedLocation) {
        visitedLocationDao.deleteVisitedLocation(visitedLocation.toEntity())
    }
    
    suspend fun deleteVisitedLocationById(id: String) {
        visitedLocationDao.deleteVisitedLocationById(id)
    }
    
    suspend fun deleteVisitedLocationsByTripId(tripId: String) {
        visitedLocationDao.deleteVisitedLocationsByTripId(tripId)
    }
    
    suspend fun deleteAutoTrackedLocations(tripId: String) {
        visitedLocationDao.deleteAutoTrackedLocations(tripId)
    }
}

fun VisitedLocationEntity.toVisitedLocation(): VisitedLocation {
    return VisitedLocation(
        id = id,
        tripId = tripId,
        name = name,
        latitude = latitude,
        longitude = longitude,
        visitedAt = visitedAt,
        duration = duration,
        photoUrl = photoUrl,
        notes = notes,
        isManual = isManual
    )
}

fun VisitedLocation.toEntity(): VisitedLocationEntity {
    return VisitedLocationEntity(
        id = id,
        tripId = tripId,
        name = name,
        latitude = latitude,
        longitude = longitude,
        visitedAt = visitedAt,
        duration = duration,
        photoUrl = photoUrl,
        notes = notes,
        isManual = isManual
    )
}
