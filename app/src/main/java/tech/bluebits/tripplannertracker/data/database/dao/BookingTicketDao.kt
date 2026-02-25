package tech.bluebits.tripplannertracker.data.database.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import tech.bluebits.tripplannertracker.data.database.entity.BookingTicketEntity

@Dao
interface BookingTicketDao {
    
    @Query("SELECT * FROM booking_tickets WHERE tripId = :tripId ORDER BY createdAt DESC")
    fun getBookingTicketsByTripId(tripId: String): Flow<List<BookingTicketEntity>>
    
    @Query("SELECT * FROM booking_tickets WHERE id = :id")
    suspend fun getBookingTicketById(id: String): BookingTicketEntity?
    
    @Query("SELECT * FROM booking_tickets WHERE bookingOptionId = :bookingOptionId")
    fun getBookingTicketsByOptionId(bookingOptionId: String): Flow<List<BookingTicketEntity>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookingTicket(bookingTicket: BookingTicketEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookingTickets(bookingTickets: List<BookingTicketEntity>)
    
    @Update
    suspend fun updateBookingTicket(bookingTicket: BookingTicketEntity)
    
    @Delete
    suspend fun deleteBookingTicket(bookingTicket: BookingTicketEntity)
    
    @Query("DELETE FROM booking_tickets WHERE id = :id")
    suspend fun deleteBookingTicketById(id: String)
    
    @Query("DELETE FROM booking_tickets WHERE tripId = :tripId")
    suspend fun deleteBookingTicketsByTripId(tripId: String)
    
    @Query("DELETE FROM booking_tickets WHERE bookingOptionId = :bookingOptionId")
    suspend fun deleteBookingTicketsByOptionId(bookingOptionId: String)
}
