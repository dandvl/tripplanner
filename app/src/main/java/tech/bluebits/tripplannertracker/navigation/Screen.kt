package tech.bluebits.tripplannertracker.navigation

const val TRIP_LIST_SCREEN = "trip_list"
const val CREATE_TRIP_SCREEN = "create_trip"
const val TRIP_DETAIL_SCREEN = "trip_detail"
const val ITINERARY_SCREEN = "itinerary"
const val EXPENSE_SCREEN = "expense"
const val MAP_SCREEN = "map"
const val JOURNAL_SCREEN = "journal"
const val BOOKING_SCREEN = "booking"

sealed class Screen(val route: String) {
    object TripList : Screen(TRIP_LIST_SCREEN)
    object CreateTrip : Screen(CREATE_TRIP_SCREEN)
    object TripDetail : Screen("$TRIP_DETAIL_SCREEN/{tripId}") {
        fun createRoute(tripId: String) = "$TRIP_DETAIL_SCREEN/$tripId"
    }
    object Itinerary : Screen("$ITINERARY_SCREEN/{tripId}") {
        fun createRoute(tripId: String) = "$ITINERARY_SCREEN/$tripId"
    }
    object Expense : Screen("$EXPENSE_SCREEN/{tripId}") {
        fun createRoute(tripId: String) = "$EXPENSE_SCREEN/$tripId"
    }
    object Map : Screen("$MAP_SCREEN/{tripId}") {
        fun createRoute(tripId: String) = "$MAP_SCREEN/$tripId"
    }
    object Journal : Screen("$JOURNAL_SCREEN/{tripId}") {
        fun createRoute(tripId: String) = "$JOURNAL_SCREEN/$tripId"
    }
    object Booking : Screen("$BOOKING_SCREEN/{tripId}") {
        fun createRoute(tripId: String) = "$BOOKING_SCREEN/$tripId"
    }
}
