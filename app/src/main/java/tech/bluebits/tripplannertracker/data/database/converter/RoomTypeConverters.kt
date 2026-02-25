package tech.bluebits.tripplannertracker.data.database.converter

import androidx.room.TypeConverter
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class RoomTypeConverters {
    
    @TypeConverter
    fun fromLocalDate(value: LocalDate?): String? {
        return value?.format(DateTimeFormatter.ISO_LOCAL_DATE)
    }
    
    @TypeConverter
    fun toLocalDate(value: String?): LocalDate? {
        return value?.let { LocalDate.parse(it, DateTimeFormatter.ISO_LOCAL_DATE) }
    }
    
    @TypeConverter
    fun fromLocalDateTime(value: LocalDateTime?): String? {
        return value?.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    }
    
    @TypeConverter
    fun toLocalDateTime(value: String?): LocalDateTime? {
        return value?.let { LocalDateTime.parse(it, DateTimeFormatter.ISO_LOCAL_DATE_TIME) }
    }
    
    @TypeConverter
    fun fromTripStatus(value: tech.bluebits.tripplannertracker.data.model.TripStatus): String {
        return value.name
    }
    
    @TypeConverter
    fun toTripStatus(value: String): tech.bluebits.tripplannertracker.data.model.TripStatus {
        return tech.bluebits.tripplannertracker.data.model.TripStatus.valueOf(value)
    }
    
    @TypeConverter
    fun fromItineraryCategory(value: tech.bluebits.tripplannertracker.data.model.ItineraryCategory): String {
        return value.name
    }
    
    @TypeConverter
    fun toItineraryCategory(value: String): tech.bluebits.tripplannertracker.data.model.ItineraryCategory {
        return tech.bluebits.tripplannertracker.data.model.ItineraryCategory.valueOf(value)
    }
    
    @TypeConverter
    fun fromExpenseCategory(value: tech.bluebits.tripplannertracker.data.model.ExpenseCategory): String {
        return value.name
    }
    
    @TypeConverter
    fun toExpenseCategory(value: String): tech.bluebits.tripplannertracker.data.model.ExpenseCategory {
        return tech.bluebits.tripplannertracker.data.model.ExpenseCategory.valueOf(value)
    }
    
    @TypeConverter
    fun fromStringList(value: List<String>): String {
        return value.joinToString(",")
    }
    
    @TypeConverter
    fun toStringList(value: String): List<String> {
        return if (value.isEmpty()) emptyList() else value.split(",")
    }
}
