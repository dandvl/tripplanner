package tech.bluebits.tripplannertracker.presentation.trip_list

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import tech.bluebits.tripplannertracker.data.model.TripStatus
import tech.bluebits.tripplannertracker.data.repository.TripRepository
import tech.bluebits.tripplannertracker.presentation.base.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class TripListViewModel @Inject constructor(
    private val tripRepository: TripRepository
) : BaseViewModel<TripListState, TripListIntent, TripListEffect>(TripListState()) {

    init {
        sendIntent(TripListIntent.LoadTrips)
    }

    override fun handleIntent(intent: TripListIntent) {
        when (intent) {
            is TripListIntent.LoadTrips -> loadTrips()
            is TripListIntent.RefreshTrips -> refreshTrips()
            is TripListIntent.DeleteTrip -> deleteTrip(intent.tripId)
            is TripListIntent.NavigateToTripDetail -> navigateToTripDetail(intent.tripId)
            is TripListIntent.NavigateToCreateTrip -> navigateToCreateTrip()
        }
    }

    private fun loadTrips() {
        viewModelScope.launch {
            updateState { copy(isLoading = true, error = null) }
            
            try {
                combine(
                    tripRepository.getUpcomingTrips(),
                    flow { emit(tripRepository.getActiveTrip()) },
                    tripRepository.getPastTrips()
                ) { upcoming, active, past ->
                    Triple(upcoming, active, past)
                }.collect { (upcoming, active, past) ->
                    updateState {
                        copy(
                            isLoading = false,
                            upcomingTrips = upcoming,
                            activeTrip = active,
                            pastTrips = past
                        )
                    }
                }
            } catch (e: Exception) {
                updateState {
                    copy(
                        isLoading = false,
                        error = e.message ?: "Failed to load trips"
                    )
                }
                sendEffect(TripListEffect.ShowError(e.message ?: "Failed to load trips"))
            }
        }
    }

    private fun refreshTrips() {
        loadTrips()
    }

    private fun deleteTrip(tripId: String) {
        viewModelScope.launch {
            try {
                tripRepository.deleteTripById(tripId)
                // Refresh the list after deletion
                loadTrips()
            } catch (e: Exception) {
                sendEffect(TripListEffect.ShowError(e.message ?: "Failed to delete trip"))
            }
        }
    }

    private fun navigateToTripDetail(tripId: String) {
        sendEffect(TripListEffect.NavigateToTripDetail(tripId))
    }

    private fun navigateToCreateTrip() {
        sendEffect(TripListEffect.NavigateToCreateTrip)
    }
}
