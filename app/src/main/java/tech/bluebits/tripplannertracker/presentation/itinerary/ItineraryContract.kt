package tech.bluebits.tripplannertracker.presentation.itinerary

import tech.bluebits.tripplannertracker.data.model.ItineraryCategory
import tech.bluebits.tripplannertracker.data.model.ItineraryItem
import tech.bluebits.tripplannertracker.presentation.base.UiState
import tech.bluebits.tripplannertracker.presentation.base.UiIntent
import tech.bluebits.tripplannertracker.presentation.base.UiEffect
import java.time.LocalDate

data class ItineraryState(
    val isLoading: Boolean = false,
    val tripId: String = "",
    val itineraryItems: List<ItineraryItem> = emptyList(),
    val selectedDate: LocalDate = LocalDate.now(),
    val selectedCategory: ItineraryCategory? = null,
    val showCompletedItems: Boolean = true,
    val error: String? = null
) : UiState

sealed class ItineraryIntent : UiIntent {
    object LoadItineraryItems : ItineraryIntent()
    data class LoadItineraryItemsForDate(val date: LocalDate) : ItineraryIntent()
    data class FilterByCategory(val category: ItineraryCategory?) : ItineraryIntent()
    data class ToggleCompletedVisibility : ItineraryIntent()
    data class AddItineraryItem(val item: ItineraryItem) : ItineraryIntent()
    data class UpdateItineraryItem(val item: ItineraryItem) : ItineraryIntent()
    data class DeleteItineraryItem(val itemId: String) : ItineraryIntent()
    data class ToggleItemCompletion(val itemId: String) : ItineraryIntent()
    data class ReorderItems(val fromIndex: Int, val toIndex: Int) : ItineraryIntent()
    data class NavigateToAddItem : ItineraryIntent()
    data class NavigateToEditItem(val itemId: String) : ItineraryIntent()
    object NavigateToMap : ItineraryIntent()
}

sealed class ItineraryEffect : UiEffect {
    data class ShowError(val message: String) : ItineraryEffect()
    object NavigateToAddItem : ItineraryEffect()
    data class NavigateToEditItem(val itemId: String) : ItineraryEffect()
    object NavigateToMap : ItineraryEffect()
    object ShowItemAdded : ItineraryEffect()
    object ShowItemUpdated : ItineraryEffect()
    object ShowItemDeleted : ItineraryEffect()
}
