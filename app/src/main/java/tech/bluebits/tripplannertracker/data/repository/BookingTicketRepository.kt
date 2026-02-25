package tech.bluebits.tripplannertracker.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import tech.bluebits.tripplannertracker.data.database.dao.BookingTicketDao
import tech.bluebits.tripplannertracker.data.database.entity.BookingTicketEntity
import tech.bluebits.tripplannertracker.data.model.BookingTicket
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookingTicketRepository @Inject constructor(
    private val bookingTicketDao: BookingTicketDao
) {
    
    fun getBookingTicketsByTripId(tripId: String): Flow<List<BookingTicket>> {
        return bookingTicketDao.getBookingTicketsByTripId(tripId).map { entities ->
            entities.map { it.toBookingTicket() }
        }
    }
    
    suspend fun getBookingTicketById(id: String): BookingTicket? {
        return bookingTicketDao.getBookingTicketById(id)?.toBookingTicket()
    }
    
    fun getBookingTicketsByOptionId(bookingOptionId: String): Flow<List<BookingTicket>> {
        return bookingTicketDao.getBookingTicketsByOptionId(bookingOptionId).map { entities ->
            entities.map { it.toBookingTicket() }
        }
    }
    
    suspend fun insertBookingTicket(bookingTicket: BookingTicket) {
        bookingTicketDao.insertBookingTicket(bookingTicket.toEntity())
    }
    
    suspend fun insertBookingTickets(bookingTickets: List<BookingTicket>) {
        bookingTicketDao.insertBookingTickets(bookingTickets.map { it.toEntity() })
    }
    
    suspend fun updateBookingTicket(bookingTicket: BookingTicket) {
        bookingTicketDao.updateBookingTicket(bookingTicket.toEntity())
    }
    
    suspend fun deleteBookingTicket(bookingTicket: BookingTicket) {
        bookingTicketDao.deleteBookingTicket(bookingTicket.toEntity())
    }
    
    suspend fun deleteBookingTicketById(id: String) {
        bookingTicketDao.deleteBookingTicketById(id)
    }
    
    suspend fun deleteBookingTicketsByTripId(tripId: String) {
        bookingTicketDao.deleteBookingTicketsByTripId(tripId)
    }
    
    suspend fun deleteBookingTicketsByOptionId(bookingOptionId: String) {
        bookingTicketDao.deleteBookingTicketsByOptionId(bookingOptionId)
    }
}

fun BookingTicketEntity.toBookingTicket(): BookingTicket {
    return BookingTicket(
        id = id,
        tripId = tripId,
        bookingOptionId = bookingOptionId,
        confirmationCode = confirmationCode,
        ticketImageUrl = ticketImageUrl,
        pdfUrl = pdfUrl,
        notes = notes,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun BookingTicket.toEntity(): BookingTicketEntity {
    return BookingTicketEntity(
        id = id,
        tripId = tripId,
        bookingOptionId = bookingOptionId,
        confirmationCode = confirmationCode,
        ticketImageUrl = ticketImageUrl,
        pdfUrl = pdfUrl,
        notes = notes,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}
