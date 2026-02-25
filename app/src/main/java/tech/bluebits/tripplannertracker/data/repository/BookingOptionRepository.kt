package tech.bluebits.tripplannertracker.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import tech.bluebits.tripplannertracker.data.database.dao.BookingOptionDao
import tech.bluebits.tripplannertracker.data.database.entity.BookingOptionEntity
import tech.bluebits.tripplannertracker.data.model.BookingOption
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookingOptionRepository @Inject constructor(
    private val bookingOptionDao: BookingOptionDao
) {
    
    fun getBookingOptionsByTripId(tripId: String): Flow<List<BookingOption>> {
        return bookingOptionDao.getBookingOptionsByTripId(tripId).map { entities ->
            entities.map { it.toBookingOption() }
        }
    }
    
    suspend fun getBookingOptionById(id: String): BookingOption? {
        return bookingOptionDao.getBookingOptionById(id)?.toBookingOption()
    }
    
    fun getBookingOptionsByType(tripId: String, type: String): Flow<List<BookingOption>> {
        return bookingOptionDao.getBookingOptionsByType(tripId, type).map { entities ->
            entities.map { it.toBookingOption() }
        }
    }
    
    fun getBookingOptionsBySelectionStatus(tripId: String, isSelected: Boolean): Flow<List<BookingOption>> {
        return bookingOptionDao.getBookingOptionsBySelectionStatus(tripId, isSelected).map { entities ->
            entities.map { it.toBookingOption() }
        }
    }
    
    suspend fun insertBookingOption(bookingOption: BookingOption) {
        bookingOptionDao.insertBookingOption(bookingOption.toEntity())
    }
    
    suspend fun insertBookingOptions(bookingOptions: List<BookingOption>) {
        bookingOptionDao.insertBookingOptions(bookingOptions.map { it.toEntity() })
    }
    
    suspend fun updateBookingOption(bookingOption: BookingOption) {
        bookingOptionDao.updateBookingOption(bookingOption.toEntity())
    }
    
    suspend fun deleteBookingOption(bookingOption: BookingOption) {
        bookingOptionDao.deleteBookingOption(bookingOption.toEntity())
    }
    
    suspend fun deleteBookingOptionById(id: String) {
        bookingOptionDao.deleteBookingOptionById(id)
    }
    
    suspend fun deleteBookingOptionsByTripId(tripId: String) {
        bookingOptionDao.deleteBookingOptionsByTripId(tripId)
    }
    
    suspend fun updateBookingOptionSelection(id: String, isSelected: Boolean) {
        bookingOptionDao.updateBookingOptionSelection(id, isSelected)
    }
    
    suspend fun clearSelectionForType(tripId: String, type: String) {
        bookingOptionDao.clearSelectionForType(tripId, type)
    }
}

fun BookingOptionEntity.toBookingOption(): BookingOption {
    return BookingOption(
        id = id,
        tripId = tripId,
        type = type,
        title = title,
        provider = provider,
        price = price,
        currency = currency,
        bookingUrl = bookingUrl,
        description = description,
        imageUrl = imageUrl,
        isSelected = isSelected,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun BookingOption.toEntity(): BookingOptionEntity {
    return BookingOptionEntity(
        id = id,
        tripId = tripId,
        type = type,
        title = title,
        provider = provider,
        price = price,
        currency = currency,
        bookingUrl = bookingUrl,
        description = description,
        imageUrl = imageUrl,
        isSelected = isSelected,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}
