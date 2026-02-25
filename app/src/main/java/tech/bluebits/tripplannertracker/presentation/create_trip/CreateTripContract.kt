package tech.bluebits.tripplannertracker.presentation.create_trip

import tech.bluebits.tripplannertracker.presentation.base.UiState
import tech.bluebits.tripplannertracker.presentation.base.UiIntent
import tech.bluebits.tripplannertracker.presentation.base.UiEffect
import java.time.LocalDate

data class CreateTripState(
    val isLoading: Boolean = false,
    val tripName: String = "",
    val destination: String = "",
    val startDate: LocalDate = LocalDate.now(),
    val endDate: LocalDate = LocalDate.now().plusDays(7),
    val notes: String = "",
    val totalBudget: String = "",
    val currency: String = "USD",
    val coverImageUrl: String? = null,
    val error: String? = null,
    val isSaved: Boolean = false
) : UiState

sealed class CreateTripIntent : UiIntent {
    data class UpdateTripName(val name: String) : CreateTripIntent()
    data class UpdateDestination(val destination: String) : CreateTripIntent()
    data class UpdateStartDate(val date: LocalDate) : CreateTripIntent()
    data class UpdateEndDate(val date: LocalDate) : CreateTripIntent()
    data class UpdateNotes(val notes: String) : CreateTripIntent()
    data class UpdateTotalBudget(val budget: String) : CreateTripIntent()
    data class UpdateCurrency(val currency: String) : CreateTripIntent()
    data class UpdateCoverImage(val imageUrl: String?) : CreateTripIntent()
    object SaveTrip : CreateTripIntent()
    object NavigateBack : CreateTripIntent()
}

sealed class CreateTripEffect : UiEffect {
    object NavigateBack : CreateTripEffect()
    data class ShowError(val message: String) : CreateTripEffect()
    object ShowSaveSuccess : CreateTripEffect()
}
