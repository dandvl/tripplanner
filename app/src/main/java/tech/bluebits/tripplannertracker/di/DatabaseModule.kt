package tech.bluebits.tripplannertracker.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import tech.bluebits.tripplannertracker.data.database.TripPlannerDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideTripPlannerDatabase(@ApplicationContext context: Context): TripPlannerDatabase {
        return Room.databaseBuilder(
            context,
            TripPlannerDatabase::class.java,
            "trip_planner_database"
        ).build()
    }

    @Provides
    fun provideTripDao(database: TripPlannerDatabase) = database.tripDao()

    @Provides
    fun provideItineraryItemDao(database: TripPlannerDatabase) = database.itineraryItemDao()

    @Provides
    fun provideExpenseDao(database: TripPlannerDatabase) = database.expenseDao()

    @Provides
    fun provideVisitedLocationDao(database: TripPlannerDatabase) = database.visitedLocationDao()

    @Provides
    fun provideJournalEntryDao(database: TripPlannerDatabase) = database.journalEntryDao()

    @Provides
    fun provideBookingOptionDao(database: TripPlannerDatabase) = database.bookingOptionDao()

    @Provides
    fun provideBookingTicketDao(database: TripPlannerDatabase) = database.bookingTicketDao()
}
