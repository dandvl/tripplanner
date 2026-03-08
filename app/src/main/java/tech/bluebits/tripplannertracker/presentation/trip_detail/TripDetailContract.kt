package tech.bluebits.tripplannertracker.presentation.trip_detail

import tech.bluebits.tripplannertracker.data.model.Trip
import tech.bluebits.tripplannertracker.presentation.base.UiEffect
import tech.bluebits.tripplannertracker.presentation.base.UiIntent
import tech.bluebits.tripplannertracker.presentation.base.UiState

data class TripDetailState(
    val isLoading: Boolean = false,
    val tripId: String = "",
    val trip: Trip? = null,
    val error: String? = null
) : UiState

sealed class TripDetailIntent : UiIntent {
    object LoadTrip : TripDetailIntent()
    object NavigateBack : TripDetailIntent()
}

sealed class TripDetailEffect : UiEffect {
    data class ShowError(val message: String) : TripDetailEffect()
    object NavigateBack : TripDetailEffect()
}
