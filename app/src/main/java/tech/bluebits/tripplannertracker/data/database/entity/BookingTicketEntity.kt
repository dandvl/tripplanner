package tech.bluebits.tripplannertracker.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import java.time.LocalDateTime

@Entity(
    tableName = "booking_tickets",
    foreignKeys = [
        ForeignKey(
            entity = TripEntity::class,
            parentColumns = ["id"],
            childColumns = ["tripId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = BookingOptionEntity::class,
            parentColumns = ["id"],
            childColumns = ["bookingOptionId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class BookingTicketEntity(
    @PrimaryKey
    val id: String,
    val tripId: String,
    val bookingOptionId: String,
    val confirmationCode: String,
    val ticketImageUrl: String?,
    val pdfUrl: String?,
    val notes: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)
