package tech.bluebits.tripplannertracker.presentation.trip_list

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import tech.bluebits.tripplannertracker.data.model.TripStatus
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripListScreen(
    viewModel: TripListViewModel = hiltViewModel(),
    onNavigateToTripDetail: (String) -> Unit,
    onNavigateToCreateTrip: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    
    LaunchedEffect(Unit) {
        viewModel.sendIntent(TripListIntent.LoadTrips)
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "My Trips",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            
            Row {
                IconButton(
                    onClick = { viewModel.sendIntent(TripListIntent.RefreshTrips) }
                ) {
                    Icon(Icons.Default.Refresh, contentDescription = "Refresh")
                }
                
                FloatingActionButton(
                    onClick = { viewModel.sendIntent(TripListIntent.NavigateToCreateTrip) },
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Trip")
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        if (state.isLoading) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Active Trip
                state.activeTrip?.let { activeTrip ->
                    item {
                        TripSection(
                            title = "Active Trip",
                            trips = listOf(activeTrip),
                            onTripClick = { tripId ->
                                viewModel.sendIntent(TripListIntent.NavigateToTripDetail(tripId))
                            }
                        )
                    }
                }
                
                // Upcoming Trips
                if (state.upcomingTrips.isNotEmpty()) {
                    item {
                        TripSection(
                            title = "Upcoming Trips",
                            trips = state.upcomingTrips,
                            onTripClick = { tripId ->
                                viewModel.sendIntent(TripListIntent.NavigateToTripDetail(tripId))
                            }
                        )
                    }
                }
                
                // Past Trips
                if (state.pastTrips.isNotEmpty()) {
                    item {
                        TripSection(
                            title = "Past Trips",
                            trips = state.pastTrips,
                            onTripClick = { tripId ->
                                viewModel.sendIntent(TripListIntent.NavigateToTripDetail(tripId))
                            }
                        )
                    }
                }
                
                if (state.upcomingTrips.isEmpty() && state.pastTrips.isEmpty() && state.activeTrip == null) {
                    item {
                        EmptyState()
                    }
                }
            }
        }
    }
    
    // Handle effects
    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is TripListEffect.NavigateToTripDetail -> onNavigateToTripDetail(effect.tripId)
                is TripListEffect.NavigateToCreateTrip -> onNavigateToCreateTrip()
                is TripListEffect.ShowError -> {
                    // Show error message (could use a snackbar)
                }
                is TripListEffect.ShowDeleteConfirmation -> {
                    // Show delete confirmation dialog
                }
            }
        }
    }
}

@Composable
fun TripSection(
    title: String,
    trips: List<tech.bluebits.tripplannertracker.data.model.Trip>,
    onTripClick: (String) -> Unit
) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        trips.forEach { trip ->
            TripCard(
                trip = trip,
                onClick = { onTripClick(trip.id) }
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun TripCard(
    trip: tech.bluebits.tripplannertracker.data.model.Trip,
    onClick: () -> Unit
) {
    val dateFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy")
    
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = trip.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                
                Text(
                    text = trip.destination,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                Text(
                    text = "${trip.startDate.format(dateFormatter)} - ${trip.endDate.format(dateFormatter)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            TripStatusBadge(status = trip.status)
        }
    }
}

@Composable
fun TripStatusBadge(status: TripStatus) {
    val (text, color) = when (status) {
        TripStatus.UPCOMING -> "Upcoming" to MaterialTheme.colorScheme.primary
        TripStatus.ACTIVE -> "Active" to MaterialTheme.colorScheme.secondary
        TripStatus.COMPLETED -> "Completed" to MaterialTheme.colorScheme.tertiary
    }
    
    Surface(
        color = color.copy(alpha = 0.1f),
        shape = MaterialTheme.shapes.small
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = color,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

@Composable
fun EmptyState() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "No trips yet",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "Create your first trip to get started!",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
