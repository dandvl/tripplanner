package tech.bluebits.tripplannertracker.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import java.time.LocalDate
import java.time.LocalDateTime

@Entity(
    tableName = "journal_entries",
    foreignKeys = [
        ForeignKey(
            entity = TripEntity::class,
            parentColumns = ["id"],
            childColumns = ["tripId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class JournalEntryEntity(
    @PrimaryKey
    val id: String,
    val tripId: String,
    val date: LocalDate,
    val title: String,
    val content: String,
    val mood: String?,
    val weather: String?,
    val temperature: Double?,
    val photoUrls: List<String>,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)
