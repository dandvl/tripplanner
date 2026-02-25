package tech.bluebits.tripplannertracker.presentation.journal

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import tech.bluebits.tripplannertracker.data.model.JournalEntry
import tech.bluebits.tripplannertracker.data.repository.JournalEntryRepository
import tech.bluebits.tripplannertracker.presentation.base.BaseViewModel
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class JournalViewModel @Inject constructor(
    private val journalEntryRepository: JournalEntryRepository
) : BaseViewModel<JournalState, JournalIntent, JournalEffect>(JournalState()) {

    override fun handleIntent(intent: JournalIntent) {
        when (intent) {
            is JournalIntent.LoadJournalEntries -> loadJournalEntries()
            is JournalIntent.LoadJournalEntryByDate -> loadJournalEntryByDate(intent.date)
            is JournalIntent.AddJournalEntry -> addJournalEntry(intent.entry)
            is JournalIntent.UpdateJournalEntry -> updateJournalEntry(intent.entry)
            is JournalIntent.DeleteJournalEntry -> deleteJournalEntry(intent.entryId)
            is JournalIntent.SelectDate -> selectDate(intent.date)
            is JournalIntent.SelectEntry -> selectEntry(intent.entry)
            is JournalIntent.StartAddingEntry -> startAddingEntry()
            is JournalIntent.StartEditingEntry -> startEditingEntry()
            is JournalIntent.CancelEditing -> cancelEditing()
            is JournalIntent.CapturePhoto -> capturePhoto()
            is JournalIntent.GetWeatherSnapshot -> getWeatherSnapshot()
            is JournalIntent.NavigateToPhotoGallery -> navigateToPhotoGallery()
            is JournalIntent.ExportJournal -> exportJournal()
        }
    }

    private fun loadJournalEntries() {
        if (state.value.tripId.isEmpty()) return
        
        viewModelScope.launch {
            updateState { copy(isLoading = true, error = null) }
            
            try {
                journalEntryRepository.getJournalEntriesByTripId(state.value.tripId)
                    .catch { e ->
                        updateState {
                            copy(
                                isLoading = false,
                                error = e.message ?: "Failed to load journal entries"
                            )
                        }
                        sendEffect(JournalEffect.ShowError(e.message ?: "Failed to load journal entries"))
                    }
                    .collect { entries ->
                        updateState {
                            copy(
                                isLoading = false,
                                journalEntries = entries
                            )
                        }
                    }
            } catch (e: Exception) {
                updateState {
                    copy(
                        isLoading = false,
                        error = e.message ?: "Failed to load journal entries"
                    )
                }
                sendEffect(JournalEffect.ShowError(e.message ?: "Failed to load journal entries"))
            }
        }
    }

    private fun loadJournalEntryByDate(date: LocalDate) {
        if (state.value.tripId.isEmpty()) return
        
        viewModelScope.launch {
            updateState { copy(isLoading = true, selectedDate = date, error = null) }
            
            try {
                val entry = journalEntryRepository.getJournalEntryByDate(state.value.tripId, date)
                updateState {
                    copy(
                        isLoading = false,
                        selectedEntry = entry
                    )
                }
            } catch (e: Exception) {
                updateState {
                    copy(
                        isLoading = false,
                        error = e.message ?: "Failed to load journal entry"
                    )
                }
                sendEffect(JournalEffect.ShowError(e.message ?: "Failed to load journal entry"))
            }
        }
    }

    private fun addJournalEntry(entry: JournalEntry) {
        viewModelScope.launch {
            try {
                val newEntry = entry.copy(
                    id = UUID.randomUUID().toString(),
                    createdAt = LocalDateTime.now(),
                    updatedAt = LocalDateTime.now()
                )
                journalEntryRepository.insertJournalEntry(newEntry)
                sendEffect(JournalEffect.ShowEntryAdded)
                loadJournalEntries() // Refresh the list
                updateState { copy(isAddingEntry = false) }
            } catch (e: Exception) {
                sendEffect(JournalEffect.ShowError(e.message ?: "Failed to add journal entry"))
            }
        }
    }

    private fun updateJournalEntry(entry: JournalEntry) {
        viewModelScope.launch {
            try {
                val updatedEntry = entry.copy(
                    updatedAt = LocalDateTime.now()
                )
                journalEntryRepository.updateJournalEntry(updatedEntry)
                sendEffect(JournalEffect.ShowEntryUpdated)
                loadJournalEntries() // Refresh the list
                updateState { copy(isEditingEntry = false, selectedEntry = updatedEntry) }
            } catch (e: Exception) {
                sendEffect(JournalEffect.ShowError(e.message ?: "Failed to update journal entry"))
            }
        }
    }

    private fun deleteJournalEntry(entryId: String) {
        viewModelScope.launch {
            try {
                journalEntryRepository.deleteJournalEntryById(entryId)
                sendEffect(JournalEffect.ShowEntryDeleted)
                loadJournalEntries() // Refresh the list
                updateState { copy(selectedEntry = null) }
            } catch (e: Exception) {
                sendEffect(JournalEffect.ShowError(e.message ?: "Failed to delete journal entry"))
            }
        }
    }

    private fun selectDate(date: LocalDate) {
        updateState { copy(selectedDate = date) }
        loadJournalEntryByDate(date)
    }

    private fun selectEntry(entry: JournalEntry) {
        updateState { copy(selectedEntry = entry, selectedDate = entry.date) }
    }

    private fun startAddingEntry() {
        updateState { 
            copy(
                isAddingEntry = true,
                isEditingEntry = false,
                selectedEntry = null
            ) 
        }
    }

    private fun startEditingEntry() {
        state.value.selectedEntry?.let { entry ->
            updateState { 
                copy(
                    isEditingEntry = true,
                    isAddingEntry = false
                ) 
            }
        }
    }

    private fun cancelEditing() {
        updateState { 
            copy(
                isAddingEntry = false,
                isEditingEntry = false
            ) 
        }
    }

    private fun capturePhoto() {
        state.value.selectedEntry?.let { entry ->
            sendEffect(JournalEffect.ShowCamera(entry.id))
        }
    }

    private fun getWeatherSnapshot() {
        state.value.selectedEntry?.let { entry ->
            sendEffect(JournalEffect.ShowWeatherDialog(entry.id))
        }
    }

    private fun navigateToPhotoGallery() {
        state.value.selectedEntry?.let { entry ->
            sendEffect(JournalEffect.NavigateToPhotoGallery(entry.id))
        }
    }

    private fun exportJournal() {
        viewModelScope.launch {
            try {
                // TODO: Implement journal export functionality
                val filePath = "journal_${state.value.tripId}_${System.currentTimeMillis()}.pdf"
                sendEffect(JournalEffect.ShowExportSuccess(filePath))
            } catch (e: Exception) {
                sendEffect(JournalEffect.ShowError(e.message ?: "Failed to export journal"))
            }
        }
    }

    fun setTripId(tripId: String) {
        updateState { copy(tripId = tripId) }
        loadJournalEntries()
    }
}
