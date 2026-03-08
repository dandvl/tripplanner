package tech.bluebits.tripplannertracker.presentation.trip_detail

import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripDetailScreen(
    tripId: String,
    onNavigateBack: () -> Unit,
    viewModel: TripDetailViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(tripId) {
        viewModel.setTripId(tripId)
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is TripDetailEffect.NavigateBack -> onNavigateBack()
                is TripDetailEffect.ShowError -> {
                    // no-op for now
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Trip Details") },
                navigationIcon = {
                    IconButton(onClick = { viewModel.sendIntent(TripDetailIntent.NavigateBack) }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            when {
                state.isLoading -> {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                state.error != null -> {
                    Text(
                        text = state.error ?: "",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                state.trip != null -> {
                    val trip = state.trip!!
                    val formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy")
                    val dateTimeFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy • HH:mm")

                    Text(
                        text = trip.name,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    InfoRow(label = "Trip ID", value = trip.id)
                    InfoRow(label = "Destination", value = trip.destination)
                    InfoRow(label = "Status", value = trip.status.name)
                    InfoRow(
                        label = "Dates",
                        value = "${trip.startDate.format(formatter)} - ${trip.endDate.format(formatter)}"
                    )
                    InfoRow(
                        label = "Budget",
                        value = "${trip.currency} ${trip.totalBudget}"
                    )
                    InfoRow(label = "Cover Image URL", value = trip.coverImageUrl ?: "—")
                    InfoRow(label = "Created", value = trip.createdAt.format(dateTimeFormatter))
                    InfoRow(label = "Updated", value = trip.updatedAt.format(dateTimeFormatter))

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Notes",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = trip.notes.ifBlank { "—" },
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                else -> {
                    Text(text = "Trip not found")
                }
            }
        }
    }
}

@Composable
private fun InfoRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.weight(0.4f)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(0.6f)
        )
    }
}
