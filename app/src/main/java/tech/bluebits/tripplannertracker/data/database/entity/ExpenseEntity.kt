package tech.bluebits.tripplannertracker.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import tech.bluebits.tripplannertracker.data.model.ExpenseCategory
import java.time.LocalDate
import java.time.LocalDateTime

@Entity(
    tableName = "expenses",
    foreignKeys = [
        ForeignKey(
            entity = TripEntity::class,
            parentColumns = ["id"],
            childColumns = ["tripId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ExpenseEntity(
    @PrimaryKey
    val id: String,
    val tripId: String,
    val title: String,
    val category: ExpenseCategory,
    val amount: Double,
    val currency: String,
    val date: LocalDate,
    val receiptImageUrl: String?,
    val latitude: Double?,
    val longitude: Double?,
    val notes: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)
