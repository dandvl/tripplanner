package tech.bluebits.tripplannertracker.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import tech.bluebits.tripplannertracker.presentation.trip_list.TripListScreen
import tech.bluebits.tripplannertracker.presentation.create_trip.CreateTripScreen

@Composable
fun TripPlannerNavigation(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.TripList.route
    ) {
        composable(Screen.TripList.route) {
            TripListScreen(
                onNavigateToTripDetail = { tripId ->
                    navController.navigate(Screen.TripDetail.createRoute(tripId))
                },
                onNavigateToCreateTrip = {
                    navController.navigate(Screen.CreateTrip.route)
                }
            )
        }
        
        composable(Screen.CreateTrip.route) {
            CreateTripScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onShowDatePicker = { date ->
                    // TODO: Implement date picker
                },
                onShowImagePicker = {
                    // TODO: Implement image picker
                }
            )
        }
        
        composable(
            route = Screen.TripDetail.route,
            arguments = listOf(navArgument("tripId") { type = NavType.StringType })
        ) { backStackEntry ->
            val tripId = backStackEntry.arguments?.getString("tripId") ?: return@composable
            // TripDetailScreen will be implemented here
            // TripDetailScreen(
            //     tripId = tripId,
            //     navController = navController
            // )
        }
        
        composable(
            route = Screen.Itinerary.route,
            arguments = listOf(navArgument("tripId") { type = NavType.StringType })
        ) { backStackEntry ->
            val tripId = backStackEntry.arguments?.getString("tripId") ?: return@composable
            // ItineraryScreen will be implemented here
            // ItineraryScreen(
            //     tripId = tripId,
            //     navController = navController
            // )
        }
        
        composable(
            route = Screen.Expense.route,
            arguments = listOf(navArgument("tripId") { type = NavType.StringType })
        ) { backStackEntry ->
            val tripId = backStackEntry.arguments?.getString("tripId") ?: return@composable
            // ExpenseScreen will be implemented here
            // ExpenseScreen(
            //     tripId = tripId,
            //     navController = navController
            // )
        }
        
        composable(
            route = Screen.Map.route,
            arguments = listOf(navArgument("tripId") { type = NavType.StringType })
        ) { backStackEntry ->
            val tripId = backStackEntry.arguments?.getString("tripId") ?: return@composable
            // MapScreen will be implemented here
            // MapScreen(
            //     tripId = tripId,
            //     navController = navController
            // )
        }
        
        composable(
            route = Screen.Journal.route,
            arguments = listOf(navArgument("tripId") { type = NavType.StringType })
        ) { backStackEntry ->
            val tripId = backStackEntry.arguments?.getString("tripId") ?: return@composable
            // JournalScreen will be implemented here
            // JournalScreen(
            //     tripId = tripId,
            //     navController = navController
            // )
        }
        
        composable(
            route = Screen.Booking.route,
            arguments = listOf(navArgument("tripId") { type = NavType.StringType })
        ) { backStackEntry ->
            val tripId = backStackEntry.arguments?.getString("tripId") ?: return@composable
            // BookingScreen will be implemented here
            // BookingScreen(
            //     tripId = tripId,
            //     navController = navController
            // )
        }
    }
}
