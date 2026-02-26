package tech.bluebits.tripplannertracker.presentation.create_trip

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import tech.bluebits.tripplannertracker.data.model.Trip
import tech.bluebits.tripplannertracker.data.model.TripStatus
import tech.bluebits.tripplannertracker.data.repository.TripRepository
import tech.bluebits.tripplannertracker.presentation.base.BaseViewModel
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class CreateTripViewModel @Inject constructor(
    private val tripRepository: TripRepository
) : BaseViewModel<CreateTripState, CreateTripIntent, CreateTripEffect>(CreateTripState()) {

    override fun handleIntent(intent: CreateTripIntent) {
        when (intent) {
            is CreateTripIntent.UpdateTripName -> updateTripName(intent.name)
            is CreateTripIntent.UpdateDestination -> updateDestination(intent.destination)
            is CreateTripIntent.UpdateStartDate -> updateStartDate(intent.date)
            is CreateTripIntent.UpdateEndDate -> updateEndDate(intent.date)
            is CreateTripIntent.UpdateNotes -> updateNotes(intent.notes)
            is CreateTripIntent.UpdateTotalBudget -> updateTotalBudget(intent.budget)
            is CreateTripIntent.UpdateCurrency -> updateCurrency(intent.currency)
            is CreateTripIntent.UpdateCoverImage -> updateCoverImage(intent.imageUrl)
            is CreateTripIntent.SaveTrip -> saveTrip()
            is CreateTripIntent.NavigateBack -> navigateBack()
        }
    }

    private fun updateTripName(name: String) {
        updateState { copy(tripName = name, error = null) }
    }

    private fun updateDestination(destination: String) {
        updateState { copy(destination = destination, error = null) }
    }

    private fun updateStartDate(date: LocalDate) {
        updateState { copy(startDate = date, error = null) }
    }

    private fun updateEndDate(date: LocalDate) {
        updateState { copy(endDate = date, error = null) }
    }

    private fun updateNotes(notes: String) {
        updateState { copy(notes = notes, error = null) }
    }

    private fun updateTotalBudget(budget: String) {
        updateState { copy(totalBudget = budget, error = null) }
    }

    private fun updateCurrency(currency: String) {
        updateState { copy(currency = currency, error = null) }
    }

    private fun updateCoverImage(imageUrl: String?) {
        updateState { copy(coverImageUrl = imageUrl, error = null) }
    }

    private fun saveTrip() {
        val currentState = state.value
        
        // Validate input
        if (currentState.tripName.isBlank()) {
            updateState { copy(error = "Trip name is required") }
            return
        }
        
        if (currentState.destination.isBlank()) {
            updateState { copy(error = "Destination is required") }
            return
        }
        
        if (currentState.startDate.isAfter(currentState.endDate)) {
            updateState { copy(error = "Start date must be before end date") }
            return
        }
        
        val budget = currentState.totalBudget.toDoubleOrNull() ?: 0.0
        
        viewModelScope.launch {
            updateState { copy(isLoading = true, error = null) }
            
            try {
                val trip = Trip(
                    id = UUID.randomUUID().toString(),
                    name = currentState.tripName,
                    destination = currentState.destination,
                    startDate = currentState.startDate,
                    endDate = currentState.endDate,
                    notes = currentState.notes,
                    totalBudget = budget,
                    currency = currentState.currency,
                    coverImageUrl = currentState.coverImageUrl,
                    status = if (currentState.startDate.isAfter(java.time.LocalDate.now())) {
                        TripStatus.UPCOMING
                    } else {
                        TripStatus.ACTIVE
                    },
                    createdAt = LocalDateTime.now(),
                    updatedAt = LocalDateTime.now()
                )
                
                tripRepository.insertTrip(trip)
                updateState { copy(isLoading = false, isSaved = true) }
                sendEffect(CreateTripEffect.NavigateBack)
                sendEffect(CreateTripEffect.ShowSaveSuccess)
                
            } catch (e: Exception) {
                updateState {
                    copy(
                        isLoading = false,
                        error = e.message ?: "Failed to save trip"
                    )
                }
                sendEffect(CreateTripEffect.ShowError(e.message ?: "Failed to save trip"))
            }
        }
    }

    private fun navigateBack() {
        sendEffect(CreateTripEffect.NavigateBack)
    }
}
