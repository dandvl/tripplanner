package tech.bluebits.tripplannertracker.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import tech.bluebits.tripplannertracker.data.database.dao.JournalEntryDao
import tech.bluebits.tripplannertracker.data.database.entity.JournalEntryEntity
import tech.bluebits.tripplannertracker.data.model.JournalEntry
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class JournalEntryRepository @Inject constructor(
    private val journalEntryDao: JournalEntryDao
) {
    
    fun getJournalEntriesByTripId(tripId: String): Flow<List<JournalEntry>> {
        return journalEntryDao.getJournalEntriesByTripId(tripId).map { entities ->
            entities.map { it.toJournalEntry() }
        }
    }
    
    suspend fun getJournalEntryById(id: String): JournalEntry? {
        return journalEntryDao.getJournalEntryById(id)?.toJournalEntry()
    }
    
    suspend fun getJournalEntryByDate(tripId: String, date: LocalDate): JournalEntry? {
        return journalEntryDao.getJournalEntryByDate(tripId, date)?.toJournalEntry()
    }
    
    fun getJournalEntriesInDateRange(tripId: String, startDate: LocalDate, endDate: LocalDate): Flow<List<JournalEntry>> {
        return journalEntryDao.getJournalEntriesInDateRange(tripId, startDate, endDate).map { entities ->
            entities.map { it.toJournalEntry() }
        }
    }
    
    fun getJournalEntriesByMood(tripId: String, mood: String): Flow<List<JournalEntry>> {
        return journalEntryDao.getJournalEntriesByMood(tripId, mood).map { entities ->
            entities.map { it.toJournalEntry() }
        }
    }
    
    suspend fun getJournalEntryCount(tripId: String): Int {
        return journalEntryDao.getJournalEntryCount(tripId)
    }
    
    suspend fun insertJournalEntry(journalEntry: JournalEntry) {
        journalEntryDao.insertJournalEntry(journalEntry.toEntity())
    }
    
    suspend fun insertJournalEntries(journalEntries: List<JournalEntry>) {
        journalEntryDao.insertJournalEntries(journalEntries.map { it.toEntity() })
    }
    
    suspend fun updateJournalEntry(journalEntry: JournalEntry) {
        journalEntryDao.updateJournalEntry(journalEntry.toEntity())
    }
    
    suspend fun deleteJournalEntry(journalEntry: JournalEntry) {
        journalEntryDao.deleteJournalEntry(journalEntry.toEntity())
    }
    
    suspend fun deleteJournalEntryById(id: String) {
        journalEntryDao.deleteJournalEntryById(id)
    }
    
    suspend fun deleteJournalEntriesByTripId(tripId: String) {
        journalEntryDao.deleteJournalEntriesByTripId(tripId)
    }
}

fun JournalEntryEntity.toJournalEntry(): JournalEntry {
    return JournalEntry(
        id = id,
        tripId = tripId,
        date = date,
        title = title,
        content = content,
        mood = mood,
        weather = weather,
        temperature = temperature,
        photoUrls = photoUrls,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun JournalEntry.toEntity(): JournalEntryEntity {
    return JournalEntryEntity(
        id = id,
        tripId = tripId,
        date = date,
        title = title,
        content = content,
        mood = mood,
        weather = weather,
        temperature = temperature,
        photoUrls = photoUrls,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}
