package tech.bluebits.tripplannertracker.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import tech.bluebits.tripplannertracker.data.database.dao.ItineraryItemDao
import tech.bluebits.tripplannertracker.data.database.entity.ItineraryItemEntity
import tech.bluebits.tripplannertracker.data.model.ItineraryCategory
import tech.bluebits.tripplannertracker.data.model.ItineraryItem
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ItineraryRepository @Inject constructor(
    private val itineraryItemDao: ItineraryItemDao
) {
    
    fun getItineraryItemsByTripId(tripId: String): Flow<List<ItineraryItem>> {
        return itineraryItemDao.getItineraryItemsByTripId(tripId).map { entities ->
            entities.map { it.toItineraryItem() }
        }
    }
    
    fun getItineraryItemsByTripIdAndDate(tripId: String, date: LocalDate): Flow<List<ItineraryItem>> {
        return itineraryItemDao.getItineraryItemsByTripIdAndDate(tripId, date).map { entities ->
            entities.map { it.toItineraryItem() }
        }
    }
    
    suspend fun getItineraryItemById(id: String): ItineraryItem? {
        return itineraryItemDao.getItineraryItemById(id)?.toItineraryItem()
    }
    
    fun getItineraryItemsByCategory(tripId: String, category: ItineraryCategory): Flow<List<ItineraryItem>> {
        return itineraryItemDao.getItineraryItemsByCategory(tripId, category).map { entities ->
            entities.map { it.toItineraryItem() }
        }
    }
    
    fun getItineraryItemsByCompletionStatus(tripId: String, isCompleted: Boolean): Flow<List<ItineraryItem>> {
        return itineraryItemDao.getItineraryItemsByCompletionStatus(tripId, isCompleted).map { entities ->
            entities.map { it.toItineraryItem() }
        }
    }
    
    fun getItineraryItemsInDateRange(tripId: String, startDate: LocalDate, endDate: LocalDate): Flow<List<ItineraryItem>> {
        return itineraryItemDao.getItineraryItemsInDateRange(tripId, startDate, endDate).map { entities ->
            entities.map { it.toItineraryItem() }
        }
    }
    
    suspend fun insertItineraryItem(itineraryItem: ItineraryItem) {
        itineraryItemDao.insertItineraryItem(itineraryItem.toEntity())
    }
    
    suspend fun insertItineraryItems(itineraryItems: List<ItineraryItem>) {
        itineraryItemDao.insertItineraryItems(itineraryItems.map { it.toEntity() })
    }
    
    suspend fun updateItineraryItem(itineraryItem: ItineraryItem) {
        itineraryItemDao.updateItineraryItem(itineraryItem.toEntity())
    }
    
    suspend fun deleteItineraryItem(itineraryItem: ItineraryItem) {
        itineraryItemDao.deleteItineraryItem(itineraryItem.toEntity())
    }
    
    suspend fun deleteItineraryItemById(id: String) {
        itineraryItemDao.deleteItineraryItemById(id)
    }
    
    suspend fun deleteItineraryItemsByTripId(tripId: String) {
        itineraryItemDao.deleteItineraryItemsByTripId(tripId)
    }
    
    suspend fun updateItineraryItemCompletion(id: String, isCompleted: Boolean) {
        itineraryItemDao.updateItineraryItemCompletion(id, isCompleted)
    }
    
    suspend fun updateItineraryItemSortOrder(id: String, sortOrder: Int) {
        itineraryItemDao.updateItineraryItemSortOrder(id, sortOrder)
    }
    
    suspend fun getMaxSortOrder(tripId: String): Int? {
        return itineraryItemDao.getMaxSortOrder(tripId)
    }
}

// Extension functions for mapping between Entity and Model
fun ItineraryItemEntity.toItineraryItem(): ItineraryItem {
    return ItineraryItem(
        id = id,
        tripId = tripId,
        title = title,
        description = description,
        date = date,
        time = time,
        location = location,
        latitude = latitude,
        longitude = longitude,
        category = category,
        isCompleted = isCompleted,
        imageUrl = imageUrl,
        reminderTime = reminderTime,
        sortOrder = sortOrder,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun ItineraryItem.toEntity(): ItineraryItemEntity {
    return ItineraryItemEntity(
        id = id,
        tripId = tripId,
        title = title,
        description = description,
        date = date,
        time = time,
        location = location,
        latitude = latitude,
        longitude = longitude,
        category = category,
        isCompleted = isCompleted,
        imageUrl = imageUrl,
        reminderTime = reminderTime,
        sortOrder = sortOrder,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}
