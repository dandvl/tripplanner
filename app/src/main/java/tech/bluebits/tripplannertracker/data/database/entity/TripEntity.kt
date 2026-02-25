package tech.bluebits.tripplannertracker.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import tech.bluebits.tripplannertracker.data.model.TripStatus
import java.time.LocalDate
import java.time.LocalDateTime

@Entity(tableName = "trips")
data class TripEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val destination: String,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val coverImageUrl: String?,
    val notes: String,
    val status: TripStatus,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val totalBudget: Double,
    val currency: String
)
