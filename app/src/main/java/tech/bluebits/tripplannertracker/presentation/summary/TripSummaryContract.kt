package tech.bluebits.tripplannertracker.presentation.summary

import tech.bluebits.tripplannertracker.data.model.TripStatus
import tech.bluebits.tripplannertracker.presentation.base.UiState
import tech.bluebits.tripplannertracker.presentation.base.UiIntent
import tech.bluebits.tripplannertracker.presentation.base.UiEffect
import java.time.LocalDate

data class TripSummaryState(
    val isLoading: Boolean = false,
    val tripId: String = "",
    val tripName: String = "",
    val destination: String = "",
    val startDate: LocalDate = LocalDate.now(),
    val endDate: LocalDate = LocalDate.now(),
    val totalBudget: Double = 0.0,
    val totalSpent: Double = 0.0,
    val totalDistanceTraveled: Double = 0.0, // in kilometers
    val totalPlacesVisited: Int = 0,
    val totalDuration: Long = 0L, // in days
    val mostVisitedCategory: String = "",
    val expenseBreakdown: Map<String, Double> = emptyMap(),
    val itineraryItemsCompleted: Int = 0,
    val totalItineraryItems: Int = 0,
    val journalEntriesCount: Int = 0,
    val photosCount: Int = 0,
    val error: String? = null
) : UiState

sealed class TripSummaryIntent : UiIntent {
    object LoadTripSummary : TripSummaryIntent()
    object ExportToPDF : TripSummaryIntent()
    object ExportToCSV : TripSummaryIntent()
    object ShareTrip : TripSummaryIntent()
    object GenerateReport : TripSummaryIntent()
    object NavigateToGallery : TripSummaryIntent()
    object NavigateToMap : TripSummaryIntent()
    object NavigateToExpenses : TripSummaryIntent()
}

sealed class TripSummaryEffect : UiEffect {
    data class ShowError(val message: String) : TripSummaryEffect()
    data class ShowExportSuccess(val filePath: String) : TripSummaryEffect()
    data class ShowShareDialog(val shareContent: String) : TripSummaryEffect()
    object NavigateToGallery : TripSummaryEffect()
    object NavigateToMap : TripSummaryEffect()
    object NavigateToExpenses : TripSummaryEffect()
    object ShowGeneratingReport : TripSummaryEffect()
}
