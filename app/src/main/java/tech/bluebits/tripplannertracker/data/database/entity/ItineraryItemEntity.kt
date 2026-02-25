package tech.bluebits.tripplannertracker.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import tech.bluebits.tripplannertracker.data.model.ItineraryCategory
import java.time.LocalDate
import java.time.LocalDateTime

@Entity(
    tableName = "itinerary_items",
    foreignKeys = [
        ForeignKey(
            entity = TripEntity::class,
            parentColumns = ["id"],
            childColumns = ["tripId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ItineraryItemEntity(
    @PrimaryKey
    val id: String,
    val tripId: String,
    val title: String,
    val description: String,
    val date: LocalDate,
    val time: String,
    val location: String,
    val latitude: Double?,
    val longitude: Double?,
    val category: ItineraryCategory,
    val isCompleted: Boolean,
    val imageUrl: String?,
    val reminderTime: LocalDateTime?,
    val sortOrder: Int,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)
