package tech.bluebits.tripplannertracker.data.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import android.content.Context
import tech.bluebits.tripplannertracker.data.database.converter.RoomTypeConverters
import tech.bluebits.tripplannertracker.data.database.dao.*

import tech.bluebits.tripplannertracker.data.database.entity.*

@Database(
    entities = [
        TripEntity::class,
        ItineraryItemEntity::class,
        ExpenseEntity::class,
        VisitedLocationEntity::class,
        JournalEntryEntity::class,
        BookingOptionEntity::class,
        BookingTicketEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(RoomTypeConverters::class)
abstract class TripPlannerDatabase : RoomDatabase() {
    
    abstract fun tripDao(): TripDao
    abstract fun itineraryItemDao(): ItineraryItemDao
    abstract fun expenseDao(): ExpenseDao
    abstract fun visitedLocationDao(): VisitedLocationDao
    abstract fun journalEntryDao(): JournalEntryDao
    abstract fun bookingOptionDao(): BookingOptionDao
    abstract fun bookingTicketDao(): BookingTicketDao
    
    companion object {
        @Volatile
        private var INSTANCE: TripPlannerDatabase? = null
        
        fun getDatabase(context: Context): TripPlannerDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TripPlannerDatabase::class.java,
                    "trip_planner_database"
                )
                .addMigrations(MIGRATION_1_2)
                .build()
                INSTANCE = instance
                instance
            }
        }
        
        // Example migration for future schema updates
        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Add migration logic here when needed
            }
        }
    }
}
