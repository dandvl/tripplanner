package tech.bluebits.tripplannertracker.presentation.itinerary

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import tech.bluebits.tripplannertracker.data.model.ItineraryCategory
import tech.bluebits.tripplannertracker.data.model.ItineraryItem
import tech.bluebits.tripplannertracker.data.repository.ItineraryRepository
import tech.bluebits.tripplannertracker.presentation.base.BaseViewModel
import java.time.LocalDateTime
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ItineraryViewModel @Inject constructor(
    private val itineraryRepository: ItineraryRepository
) : BaseViewModel<ItineraryState, ItineraryIntent, ItineraryEffect>(ItineraryState()) {

    override fun handleIntent(intent: ItineraryIntent) {
        when (intent) {
            is ItineraryIntent.LoadItineraryItems -> loadItineraryItems()
            is ItineraryIntent.LoadItineraryItemsForDate -> loadItineraryItemsForDate(intent.date)
            is ItineraryIntent.FilterByCategory -> filterByCategory(intent.category)
            is ItineraryIntent.ToggleCompletedVisibility -> toggleCompletedVisibility()
            is ItineraryIntent.AddItineraryItem -> addItineraryItem(intent.item)
            is ItineraryIntent.UpdateItineraryItem -> updateItineraryItem(intent.item)
            is ItineraryIntent.DeleteItineraryItem -> deleteItineraryItem(intent.itemId)
            is ItineraryIntent.ToggleItemCompletion -> toggleItemCompletion(intent.itemId)
            is ItineraryIntent.ReorderItems -> reorderItems(intent.fromIndex, intent.toIndex)
            is ItineraryIntent.NavigateToAddItem -> navigateToAddItem()
            is ItineraryIntent.NavigateToEditItem -> navigateToEditItem(intent.itemId)
            is ItineraryIntent.NavigateToMap -> navigateToMap()
        }
    }

    private fun loadItineraryItems() {
        if (state.value.tripId.isEmpty()) return
        
        viewModelScope.launch {
            updateState { copy(isLoading = true, error = null) }
            
            try {
                itineraryRepository.getItineraryItemsByTripId(state.value.tripId)
                    .catch { e ->
                        updateState {
                            copy(
                                isLoading = false,
                                error = e.message ?: "Failed to load itinerary items"
                            )
                        }
                        sendEffect(ItineraryEffect.ShowError(e.message ?: "Failed to load itinerary items"))
                    }
                    .collect { items ->
                        updateState {
                            copy(
                                isLoading = false,
                                itineraryItems = items
                            )
                        }
                    }
            } catch (e: Exception) {
                updateState {
                    copy(
                        isLoading = false,
                        error = e.message ?: "Failed to load itinerary items"
                    )
                }
                sendEffect(ItineraryEffect.ShowError(e.message ?: "Failed to load itinerary items"))
            }
        }
    }

    private fun loadItineraryItemsForDate(date: LocalDate) {
        if (state.value.tripId.isEmpty()) return
        
        viewModelScope.launch {
            updateState { copy(isLoading = true, selectedDate = date, error = null) }
            
            try {
                itineraryRepository.getItineraryItemsByTripIdAndDate(state.value.tripId, date)
                    .catch { e ->
                        updateState {
                            copy(
                                isLoading = false,
                                error = e.message ?: "Failed to load itinerary items for date"
                            )
                        }
                        sendEffect(ItineraryEffect.ShowError(e.message ?: "Failed to load itinerary items for date"))
                    }
                    .collect { items ->
                        updateState {
                            copy(
                                isLoading = false,
                                itineraryItems = items
                            )
                        }
                    }
            } catch (e: Exception) {
                updateState {
                    copy(
                        isLoading = false,
                        error = e.message ?: "Failed to load itinerary items for date"
                    )
                }
                sendEffect(ItineraryEffect.ShowError(e.message ?: "Failed to load itinerary items for date"))
            }
        }
    }

    private fun filterByCategory(category: ItineraryCategory?) {
        updateState { copy(selectedCategory = category) }
        // Reload items with filter
        if (category != null) {
            filterItemsByCategory(category)
        } else {
            loadItineraryItems()
        }
    }

    private fun filterItemsByCategory(category: ItineraryCategory) {
        if (state.value.tripId.isEmpty()) return
        
        viewModelScope.launch {
            updateState { copy(isLoading = true, error = null) }
            
            try {
                itineraryRepository.getItineraryItemsByCategory(state.value.tripId, category)
                    .catch { e ->
                        updateState {
                            copy(
                                isLoading = false,
                                error = e.message ?: "Failed to filter items by category"
                            )
                        }
                        sendEffect(ItineraryEffect.ShowError(e.message ?: "Failed to filter items by category"))
                    }
                    .collect { items ->
                        updateState {
                            copy(
                                isLoading = false,
                                itineraryItems = items
                            )
                        }
                    }
            } catch (e: Exception) {
                updateState {
                    copy(
                        isLoading = false,
                        error = e.message ?: "Failed to filter items by category"
                    )
                }
                sendEffect(ItineraryEffect.ShowError(e.message ?: "Failed to filter items by category"))
            }
        }
    }

    private fun toggleCompletedVisibility() {
        updateState { copy(showCompletedItems = !showCompletedItems) }
        // Apply filter
        if (!state.value.showCompletedItems) {
            val filteredItems = state.value.itineraryItems.filter { !it.isCompleted }
            updateState { copy(itineraryItems = filteredItems) }
        } else {
            loadItineraryItems()
        }
    }

    private fun addItineraryItem(item: ItineraryItem) {
        viewModelScope.launch {
            try {
                val newItem = item.copy(
                    id = UUID.randomUUID().toString(),
                    createdAt = LocalDateTime.now(),
                    updatedAt = LocalDateTime.now()
                )
                itineraryRepository.insertItineraryItem(newItem)
                sendEffect(ItineraryEffect.ShowItemAdded)
                loadItineraryItems() // Refresh the list
            } catch (e: Exception) {
                sendEffect(ItineraryEffect.ShowError(e.message ?: "Failed to add itinerary item"))
            }
        }
    }

    private fun updateItineraryItem(item: ItineraryItem) {
        viewModelScope.launch {
            try {
                val updatedItem = item.copy(
                    updatedAt = LocalDateTime.now()
                )
                itineraryRepository.updateItineraryItem(updatedItem)
                sendEffect(ItineraryEffect.ShowItemUpdated)
                loadItineraryItems() // Refresh the list
            } catch (e: Exception) {
                sendEffect(ItineraryEffect.ShowError(e.message ?: "Failed to update itinerary item"))
            }
        }
    }

    private fun deleteItineraryItem(itemId: String) {
        viewModelScope.launch {
            try {
                itineraryRepository.deleteItineraryItemById(itemId)
                sendEffect(ItineraryEffect.ShowItemDeleted)
                loadItineraryItems() // Refresh the list
            } catch (e: Exception) {
                sendEffect(ItineraryEffect.ShowError(e.message ?: "Failed to delete itinerary item"))
            }
        }
    }

    private fun toggleItemCompletion(itemId: String) {
        viewModelScope.launch {
            try {
                val item = state.value.itineraryItems.find { it.id == itemId }
                if (item != null) {
                    val updatedItem = item.copy(
                        isCompleted = !item.isCompleted,
                        updatedAt = LocalDateTime.now()
                    )
                    itineraryRepository.updateItineraryItem(updatedItem)
                    loadItineraryItems() // Refresh the list
                }
            } catch (e: Exception) {
                sendEffect(ItineraryEffect.ShowError(e.message ?: "Failed to toggle item completion"))
            }
        }
    }

    private fun reorderItems(fromIndex: Int, toIndex: Int) {
        val currentItems = state.value.itineraryItems.toMutableList()
        if (fromIndex < currentItems.size && toIndex < currentItems.size) {
            val movedItem = currentItems.removeAt(fromIndex)
            currentItems.add(toIndex, movedItem)
            
            // Update sort orders
            val updatedItems = currentItems.mapIndexed { index, item ->
                item.copy(sortOrder = index, updatedAt = LocalDateTime.now())
            }
            
            viewModelScope.launch {
                try {
                    itineraryRepository.insertItineraryItems(updatedItems)
                    loadItineraryItems() // Refresh the list
                } catch (e: Exception) {
                    sendEffect(ItineraryEffect.ShowError(e.message ?: "Failed to reorder items"))
                }
            }
        }
    }

    private fun navigateToAddItem() {
        sendEffect(ItineraryEffect.NavigateToAddItem)
    }

    private fun navigateToEditItem(itemId: String) {
        sendEffect(ItineraryEffect.NavigateToEditItem(itemId))
    }

    private fun navigateToMap() {
        sendEffect(ItineraryEffect.NavigateToMap)
    }

    fun setTripId(tripId: String) {
        updateState { copy(tripId = tripId) }
        loadItineraryItems()
    }
}
