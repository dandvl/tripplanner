package tech.bluebits.tripplannertracker.presentation.summary

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import tech.bluebits.tripplannertracker.data.model.ExpenseCategory
import tech.bluebits.tripplannertracker.data.model.ItineraryCategory
import tech.bluebits.tripplannertracker.data.repository.*
import tech.bluebits.tripplannertracker.presentation.base.BaseViewModel
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import javax.inject.Inject

@HiltViewModel
class TripSummaryViewModel @Inject constructor(
    private val tripRepository: TripRepository,
    private val expenseRepository: ExpenseRepository,
    private val itineraryRepository: ItineraryRepository,
    private val visitedLocationRepository: VisitedLocationRepository,
    private val journalEntryRepository: JournalEntryRepository
) : BaseViewModel<TripSummaryState, TripSummaryIntent, TripSummaryEffect>(TripSummaryState()) {

    override fun handleIntent(intent: TripSummaryIntent) {
        when (intent) {
            is TripSummaryIntent.LoadTripSummary -> loadTripSummary()
            is TripSummaryIntent.ExportToPDF -> exportToPDF()
            is TripSummaryIntent.ExportToCSV -> exportToCSV()
            is TripSummaryIntent.ShareTrip -> shareTrip()
            is TripSummaryIntent.GenerateReport -> generateReport()
            is TripSummaryIntent.NavigateToGallery -> navigateToGallery()
            is TripSummaryIntent.NavigateToMap -> navigateToMap()
            is TripSummaryIntent.NavigateToExpenses -> navigateToExpenses()
        }
    }

    private fun loadTripSummary() {
        if (state.value.tripId.isEmpty()) return
        
        viewModelScope.launch {
            updateState { copy(isLoading = true, error = null) }
            
            try {
                val trip = tripRepository.getTripById(state.value.tripId)
                if (trip != null) {
                    val summary = generateTripSummary(trip)
                    updateState {
                        copy(
                            isLoading = false,
                            tripName = trip.name,
                            destination = trip.destination,
                            startDate = trip.startDate,
                            endDate = trip.endDate,
                            totalBudget = trip.totalBudget,
                            totalSpent = summary.totalSpent,
                            totalDistanceTraveled = summary.totalDistanceTraveled,
                            totalPlacesVisited = summary.totalPlacesVisited,
                            totalDuration = summary.totalDuration,
                            mostVisitedCategory = summary.mostVisitedCategory,
                            expenseBreakdown = summary.expenseBreakdown,
                            itineraryItemsCompleted = summary.itineraryItemsCompleted,
                            totalItineraryItems = summary.totalItineraryItems,
                            journalEntriesCount = summary.journalEntriesCount,
                            photosCount = summary.photosCount
                        )
                    }
                } else {
                    updateState {
                        copy(
                            isLoading = false,
                            error = "Trip not found"
                        )
                    }
                }
            } catch (e: Exception) {
                updateState {
                    copy(
                        isLoading = false,
                        error = e.message ?: "Failed to load trip summary"
                    )
                }
                sendEffect(TripSummaryEffect.ShowError(e.message ?: "Failed to load trip summary"))
            }
        }
    }

    private suspend fun generateTripSummary(trip: tech.bluebits.tripplannertracker.data.model.Trip): TripSummaryData {
        val totalSpent = expenseRepository.getTotalExpensesForTrip(trip.id) ?: 0.0
        val expenseSummary = expenseRepository.getExpenseSummaryByCategory(trip.id)
        val expenseBreakdown = expenseSummary.associate { it.category.name to it.total }
        
        val visitedLocations = visitedLocationRepository.getVisitedLocationCount(trip.id)
        val totalDistance = calculateTotalDistance(trip.id)
        
        val itineraryItems = itineraryRepository.getItineraryItemsByTripId(trip.id)
        val completedItems = itineraryItems.filter { it.isCompleted }.size
        
        val journalEntries = journalEntryRepository.getJournalEntryCount(trip.id)
        val photosCount = countPhotos(trip.id)
        
        val mostVisitedCategory = findMostVisitedCategory(expenseSummary)
        
        val duration = ChronoUnit.DAYS.between(trip.startDate, trip.endDate).toInt()
        
        return TripSummaryData(
            totalSpent = totalSpent,
            totalDistanceTraveled = totalDistance,
            totalPlacesVisited = visitedLocations,
            totalDuration = duration.toLong(),
            mostVisitedCategory = mostVisitedCategory,
            expenseBreakdown = expenseBreakdown,
            itineraryItemsCompleted = completedItems,
            totalItineraryItems = itineraryItems.size,
            journalEntriesCount = journalEntries,
            photosCount = photosCount
        )
    }

    private suspend fun calculateTotalDistance(tripId: String): Double {
        // Simplified distance calculation
        // In a real implementation, this would calculate the actual distance between visited locations
        val locations = visitedLocationRepository.getUniqueCoordinatesForTrip(tripId)
        if (locations.size < 2) return 0.0
        
        var totalDistance = 0.0
        for (i in 0 until locations.size - 1) {
            val distance = calculateDistance(
                locations[i].latitude, locations[i].longitude,
                locations[i + 1].latitude, locations[i + 1].longitude
            )
            totalDistance += distance
        }
        
        return totalDistance
    }

    private fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val earthRadius = 6371.0 // Earth's radius in kilometers
        
        val latDistance = Math.toRadians(lat2 - lat1)
        val lonDistance = Math.toRadians(lon2 - lon1)
        
        val a = Math.sin(latDistance / 2).pow(2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                Math.sin(lonDistance / 2).pow(2)
        
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
        
        return earthRadius * c
    }

    private fun findMostVisitedCategory(expenseSummary: List<tech.bluebits.tripplannertracker.data.database.dao.CategoryExpenseSummary>): String {
        return expenseSummary.maxByOrNull { it.total }?.category?.name ?: "None"
    }

    private suspend fun countPhotos(tripId: String): Int {
        // Count photos from journal entries and other sources
        val journalEntries = journalEntryRepository.getJournalEntriesByTripId(tripId)
        return journalEntries.sumOf { it.photoUrls.size }
    }

    private fun exportToPDF() {
        viewModelScope.launch {
            try {
                // TODO: Implement PDF export functionality
                val filePath = "trip_summary_${state.value.tripId}_${System.currentTimeMillis()}.pdf"
                sendEffect(TripSummaryEffect.ShowExportSuccess(filePath))
            } catch (e: Exception) {
                sendEffect(TripSummaryEffect.ShowError(e.message ?: "Failed to export to PDF"))
            }
        }
    }

    private fun exportToCSV() {
        viewModelScope.launch {
            try {
                // TODO: Implement CSV export functionality
                val filePath = "trip_expenses_${state.value.tripId}_${System.currentTimeMillis()}.csv"
                sendEffect(TripSummaryEffect.ShowExportSuccess(filePath))
            } catch (e: Exception) {
                sendEffect(TripSummaryEffect.ShowError(e.message ?: "Failed to export to CSV"))
            }
        }
    }

    private fun shareTrip() {
        val shareContent = generateShareContent()
        sendEffect(TripSummaryEffect.ShowShareDialog(shareContent))
    }

    private fun generateShareContent(): String {
        return """
            🌍 ${state.value.tripName}
            📍 ${state.value.destination}
            📅 ${state.value.startDate} - ${state.value.endDate}
            💰 Budget: $${state.value.totalBudget} | Spent: $${state.value.totalSpent}
            🏃 Distance: ${state.value.totalDistanceTraveled} km
            📍 Places Visited: ${state.value.totalPlacesVisited}
            📝 Journal Entries: ${state.value.journalEntriesCount}
            📸 Photos: ${state.value.photosCount}
            
            Created with TripPlannerTracker! 🚀
        """.trimIndent()
    }

    private fun generateReport() {
        sendEffect(TripSummaryEffect.ShowGeneratingReport)
        // TODO: Implement detailed report generation
    }

    private fun navigateToGallery() {
        sendEffect(TripSummaryEffect.NavigateToGallery)
    }

    private fun navigateToMap() {
        sendEffect(TripSummaryEffect.NavigateToMap)
    }

    private fun navigateToExpenses() {
        sendEffect(TripSummaryEffect.NavigateToExpenses)
    }

    fun setTripId(tripId: String) {
        updateState { copy(tripId = tripId) }
        loadTripSummary()
    }
}

data class TripSummaryData(
    val totalSpent: Double,
    val totalDistanceTraveled: Double,
    val totalPlacesVisited: Int,
    val totalDuration: Long,
    val mostVisitedCategory: String,
    val expenseBreakdown: Map<String, Double>,
    val itineraryItemsCompleted: Int,
    val totalItineraryItems: Int,
    val journalEntriesCount: Int,
    val photosCount: Int
)
