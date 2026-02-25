package tech.bluebits.tripplannertracker.data.database.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import tech.bluebits.tripplannertracker.data.database.entity.JournalEntryEntity
import java.time.LocalDate

@Dao
interface JournalEntryDao {
    
    @Query("SELECT * FROM journal_entries WHERE tripId = :tripId ORDER BY date DESC")
    fun getJournalEntriesByTripId(tripId: String): Flow<List<JournalEntryEntity>>
    
    @Query("SELECT * FROM journal_entries WHERE id = :id")
    suspend fun getJournalEntryById(id: String): JournalEntryEntity?
    
    @Query("SELECT * FROM journal_entries WHERE tripId = :tripId AND date = :date")
    suspend fun getJournalEntryByDate(tripId: String, date: LocalDate): JournalEntryEntity?
    
    @Query("SELECT * FROM journal_entries WHERE tripId = :tripId AND date >= :startDate AND date <= :endDate ORDER BY date DESC")
    fun getJournalEntriesInDateRange(tripId: String, startDate: LocalDate, endDate: LocalDate): Flow<List<JournalEntryEntity>>
    
    @Query("SELECT * FROM journal_entries WHERE tripId = :tripId AND mood = :mood ORDER BY date DESC")
    fun getJournalEntriesByMood(tripId: String, mood: String): Flow<List<JournalEntryEntity>>
    
    @Query("SELECT COUNT(*) FROM journal_entries WHERE tripId = :tripId")
    suspend fun getJournalEntryCount(tripId: String): Int
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertJournalEntry(journalEntry: JournalEntryEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertJournalEntries(journalEntries: List<JournalEntryEntity>)
    
    @Update
    suspend fun updateJournalEntry(journalEntry: JournalEntryEntity)
    
    @Delete
    suspend fun deleteJournalEntry(journalEntry: JournalEntryEntity)
    
    @Query("DELETE FROM journal_entries WHERE id = :id")
    suspend fun deleteJournalEntryById(id: String)
    
    @Query("DELETE FROM journal_entries WHERE tripId = :tripId")
    suspend fun deleteJournalEntriesByTripId(tripId: String)
}
