package tech.bluebits.tripplannertracker.presentation.trip_list

import tech.bluebits.tripplannertracker.data.model.Trip
import tech.bluebits.tripplannertracker.presentation.base.UiState
import tech.bluebits.tripplannertracker.presentation.base.UiIntent
import tech.bluebits.tripplannertracker.presentation.base.UiEffect

data class TripListState(
    val isLoading: Boolean = false,
    val upcomingTrips: List<Trip> = emptyList(),
    val activeTrip: Trip? = null,
    val pastTrips: List<Trip> = emptyList(),
    val error: String? = null
) : UiState

sealed class TripListIntent : UiIntent {
    object LoadTrips : TripListIntent()
    object RefreshTrips : TripListIntent()
    data class DeleteTrip(val tripId: String) : TripListIntent()
    data class NavigateToTripDetail(val tripId: String) : TripListIntent()
    object NavigateToCreateTrip : TripListIntent()
}

sealed class TripListEffect : UiEffect {
    data class ShowError(val message: String) : TripListEffect()
    data class NavigateToTripDetail(val tripId: String) : TripListEffect()
    object NavigateToCreateTrip : TripListEffect()
    object ShowDeleteConfirmation : TripListEffect()
}
