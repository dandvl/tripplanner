package tech.bluebits.tripplannertracker.presentation.journal

import tech.bluebits.tripplannertracker.data.model.JournalEntry
import tech.bluebits.tripplannertracker.presentation.base.UiState
import tech.bluebits.tripplannertracker.presentation.base.UiIntent
import tech.bluebits.tripplannertracker.presentation.base.UiEffect
import java.time.LocalDate

data class JournalState(
    val isLoading: Boolean = false,
    val tripId: String = "",
    val journalEntries: List<JournalEntry> = emptyList(),
    val selectedDate: LocalDate = LocalDate.now(),
    val selectedEntry: JournalEntry? = null,
    val isAddingEntry: Boolean = false,
    val isEditingEntry: Boolean = false,
    val moods: List<String> = listOf("Happy", "Excited", "Calm", "Tired", "Adventurous", "Relaxed", "Surprised"),
    val weatherOptions: List<String> = listOf("Sunny", "Cloudy", "Rainy", "Snowy", "Windy", "Foggy"),
    val error: String? = null
) : UiState

sealed class JournalIntent : UiIntent {
    object LoadJournalEntries : JournalIntent()
    data class LoadJournalEntryByDate(val date: LocalDate) : JournalIntent()
    data class AddJournalEntry(val entry: JournalEntry) : JournalIntent()
    data class UpdateJournalEntry(val entry: JournalEntry) : JournalIntent()
    data class DeleteJournalEntry(val entryId: String) : JournalIntent()
    data class SelectDate(val date: LocalDate) : JournalIntent()
    data class SelectEntry(val entry: JournalEntry) : JournalIntent()
    object StartAddingEntry : JournalIntent()
    object StartEditingEntry : JournalIntent()
    object CancelEditing : JournalIntent()
    data class CapturePhoto : JournalIntent()
    data class GetWeatherSnapshot : JournalIntent()
    object NavigateToPhotoGallery : JournalIntent()
    object ExportJournal : JournalIntent()
}

sealed class JournalEffect : UiEffect {
    data class ShowError(val message: String) : JournalEffect()
    object ShowEntryAdded : JournalEffect()
    object ShowEntryUpdated : JournalEffect()
    object ShowEntryDeleted : JournalEffect()
    data class NavigateToPhotoGallery(val entryId: String) : JournalEffect()
    data class ShowCamera(val entryId: String) : JournalEffect()
    data class ShowWeatherDialog(val entryId: String) : JournalEffect()
    data class ShowExportSuccess(val filePath: String) : JournalEffect()
    object ShowDatePicker : JournalEffect()
}
