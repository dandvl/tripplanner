package tech.bluebits.tripplannertracker.presentation.trip_detail

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import tech.bluebits.tripplannertracker.data.repository.TripRepository
import tech.bluebits.tripplannertracker.presentation.base.BaseViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class TripDetailViewModel @Inject constructor(
    private val tripRepository: TripRepository
) : BaseViewModel<TripDetailState, TripDetailIntent, TripDetailEffect>(TripDetailState()) {

    override fun handleIntent(intent: TripDetailIntent) {
        when (intent) {
            is TripDetailIntent.LoadTrip -> loadTrip()
            is TripDetailIntent.NavigateBack -> sendEffect(TripDetailEffect.NavigateBack)
        }
    }

    private fun loadTrip() {
        val tripId = state.value.tripId
        if (tripId.isBlank()) return

        viewModelScope.launch {
            updateState { copy(isLoading = true, error = null) }
            try {
                val trip = tripRepository.getTripById(tripId)
                if (trip == null) {
                    updateState { copy(isLoading = false, trip = null, error = "Trip not found") }
                } else {
                    updateState { copy(isLoading = false, trip = trip, error = null) }
                }
            } catch (e: Exception) {
                Timber.tag("TRP").e(e, "Failed to load trip")
                val message = e.message ?: "Failed to load trip"
                updateState { copy(isLoading = false, error = message) }
                sendEffect(TripDetailEffect.ShowError(message))
            }
        }
    }

    fun setTripId(tripId: String) {
        if (tripId == state.value.tripId) return
        updateState { copy(tripId = tripId) }
        sendIntent(TripDetailIntent.LoadTrip)
    }
}
