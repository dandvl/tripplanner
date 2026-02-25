package tech.bluebits.tripplannertracker.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import tech.bluebits.tripplannertracker.data.database.dao.*
import tech.bluebits.tripplannertracker.data.repository.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideTripRepository(
        tripDao: TripDao
    ): TripRepository {
        return TripRepository(tripDao)
    }
    
    @Provides
    @Singleton
    fun provideItineraryRepository(
        itineraryItemDao: ItineraryItemDao
    ): ItineraryRepository {
        return ItineraryRepository(itineraryItemDao)
    }
    
    @Provides
    @Singleton
    fun provideExpenseRepository(
        expenseDao: ExpenseDao
    ): ExpenseRepository {
        return ExpenseRepository(expenseDao)
    }
    
    @Provides
    @Singleton
    fun provideVisitedLocationRepository(
        visitedLocationDao: VisitedLocationDao
    ): VisitedLocationRepository {
        return VisitedLocationRepository(visitedLocationDao)
    }
    
    @Provides
    @Singleton
    fun provideJournalEntryRepository(
        journalEntryDao: JournalEntryDao
    ): JournalEntryRepository {
        return JournalEntryRepository(journalEntryDao)
    }
    
    @Provides
    @Singleton
    fun provideBookingOptionRepository(
        bookingOptionDao: BookingOptionDao
    ): BookingOptionRepository {
        return BookingOptionRepository(bookingOptionDao)
    }
    
    @Provides
    @Singleton
    fun provideBookingTicketRepository(
        bookingTicketDao: BookingTicketDao
    ): BookingTicketRepository {
        return BookingTicketRepository(bookingTicketDao)
    }
}
