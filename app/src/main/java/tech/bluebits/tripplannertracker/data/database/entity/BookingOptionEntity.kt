package tech.bluebits.tripplannertracker.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import java.time.LocalDateTime

@Entity(
    tableName = "booking_options",
    foreignKeys = [
        ForeignKey(
            entity = TripEntity::class,
            parentColumns = ["id"],
            childColumns = ["tripId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class BookingOptionEntity(
    @PrimaryKey
    val id: String,
    val tripId: String,
    val type: String,
    val title: String,
    val provider: String,
    val price: Double,
    val currency: String,
    val bookingUrl: String?,
    val description: String,
    val imageUrl: String?,
    val isSelected: Boolean,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)
