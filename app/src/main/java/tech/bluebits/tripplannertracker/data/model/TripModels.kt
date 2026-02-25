package tech.bluebits.tripplannertracker.data.model

import java.time.LocalDate
import java.time.LocalDateTime

enum class TripStatus {
    UPCOMING,
    ACTIVE,
    COMPLETED
}

enum class ItineraryCategory {
    FLIGHT,
    HOTEL,
    ACTIVITY,
    FOOD,
    TRANSPORT,
    OTHER
}

enum class ExpenseCategory {
    FLIGHT,
    HOTEL,
    FOOD,
    TRANSPORT,
    ACTIVITY,
    SHOPPING,
    ENTERTAINMENT,
    OTHER
}

data class Trip(
    val id: String = "",
    val name: String = "",
    val destination: String = "",
    val startDate: LocalDate = LocalDate.now(),
    val endDate: LocalDate = LocalDate.now(),
    val coverImageUrl: String? = null,
    val notes: String = "",
    val status: TripStatus = TripStatus.UPCOMING,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now(),
    val totalBudget: Double = 0.0,
    val currency: String = "USD"
)

data class ItineraryItem(
    val id: String = "",
    val tripId: String = "",
    val title: String = "",
    val description: String = "",
    val date: LocalDate = LocalDate.now(),
    val time: String = "",
    val location: String = "",
    val latitude: Double? = null,
    val longitude: Double? = null,
    val category: ItineraryCategory = ItineraryCategory.OTHER,
    val isCompleted: Boolean = false,
    val imageUrl: String? = null,
    val reminderTime: LocalDateTime? = null,
    val sortOrder: Int = 0,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
)

data class Expense(
    val id: String = "",
    val tripId: String = "",
    val title: String = "",
    val category: ExpenseCategory = ExpenseCategory.OTHER,
    val amount: Double = 0.0,
    val currency: String = "USD",
    val date: LocalDate = LocalDate.now(),
    val receiptImageUrl: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val notes: String = "",
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
)

data class VisitedLocation(
    val id: String = "",
    val tripId: String = "",
    val name: String = "",
    val latitude: Double,
    val longitude: Double,
    val visitedAt: LocalDateTime = LocalDateTime.now(),
    val duration: Long? = null, // in minutes
    val photoUrl: String? = null,
    val notes: String = "",
    val isManual: Boolean = false
)

data class JournalEntry(
    val id: String = "",
    val tripId: String = "",
    val date: LocalDate = LocalDate.now(),
    val title: String = "",
    val content: String = "",
    val mood: String? = null,
    val weather: String? = null,
    val temperature: Double? = null,
    val photoUrls: List<String> = emptyList(),
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
)

data class BookingOption(
    val id: String = "",
    val tripId: String = "",
    val type: String, // "flight", "hotel", "activity"
    val title: String = "",
    val provider: String = "",
    val price: Double = 0.0,
    val currency: String = "USD",
    val bookingUrl: String? = null,
    val description: String = "",
    val imageUrl: String? = null,
    val isSelected: Boolean = false,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
)

data class BookingTicket(
    val id: String = "",
    val tripId: String = "",
    val bookingOptionId: String = "",
    val confirmationCode: String = "",
    val ticketImageUrl: String? = null,
    val pdfUrl: String? = null,
    val notes: String = "",
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
)
