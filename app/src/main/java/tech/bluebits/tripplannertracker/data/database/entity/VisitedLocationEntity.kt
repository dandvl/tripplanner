package tech.bluebits.tripplannertracker.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import java.time.LocalDateTime

@Entity(
    tableName = "visited_locations",
    foreignKeys = [
        ForeignKey(
            entity = TripEntity::class,
            parentColumns = ["id"],
            childColumns = ["tripId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class VisitedLocationEntity(
    @PrimaryKey
    val id: String,
    val tripId: String,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val visitedAt: LocalDateTime,
    val duration: Long?,
    val photoUrl: String?,
    val notes: String,
    val isManual: Boolean
)
