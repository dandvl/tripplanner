package tech.bluebits.tripplannertracker.presentation.create_trip

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTripScreen(
    viewModel: CreateTripViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onShowDatePicker: (LocalDate) -> Unit,
    onShowImagePicker: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    var showStartDatePicker by remember { mutableStateOf(false) }
    var showEndDatePicker by remember { mutableStateOf(false) }
    
    // Handle effects
    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is CreateTripEffect.NavigateBack -> onNavigateBack()
                is CreateTripEffect.ShowError -> {
                    // Show error message (could use a snackbar)
                }
                is CreateTripEffect.ShowSaveSuccess -> {
                    // Show success message (could use a snackbar)
                }
            }
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Create Trip") },
                navigationIcon = {
                    IconButton(onClick = { viewModel.sendIntent(CreateTripIntent.NavigateBack) }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    if (state.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        TextButton(
                            onClick = { viewModel.sendIntent(CreateTripIntent.SaveTrip) },
                            enabled = state.tripName.isNotBlank() && state.destination.isNotBlank()
                        ) {
                            Text("Save")
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Error message
            state.error?.let { error ->
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
            
            // Trip Name
            OutlinedTextField(
                value = state.tripName,
                onValueChange = { viewModel.sendIntent(CreateTripIntent.UpdateTripName(it)) },
                label = { Text("Trip Name *") },
                modifier = Modifier.fillMaxWidth(),
                enabled = !state.isLoading,
                singleLine = true
            )
            
            // Destination
            OutlinedTextField(
                value = state.destination,
                onValueChange = { viewModel.sendIntent(CreateTripIntent.UpdateDestination(it)) },
                label = { Text("Destination *") },
                modifier = Modifier.fillMaxWidth(),
                enabled = !state.isLoading,
                singleLine = true
            )
            
            // Date Range
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Start Date
                OutlinedTextField(
                    value = state.startDate.format(DateTimeFormatter.ofPattern("MMM dd, yyyy")),
                    onValueChange = { },
                    label = { Text("Start Date") },
                    modifier = Modifier.weight(1f),
                    enabled = !state.isLoading,
                    readOnly = true,
                    trailingIcon = {
                        IconButton(onClick = { showStartDatePicker = true }) {
                            Icon(Icons.Default.CalendarToday, contentDescription = "Select Start Date")
                        }
                    }
                )
                
                // End Date
                OutlinedTextField(
                    value = state.endDate.format(DateTimeFormatter.ofPattern("MMM dd, yyyy")),
                    onValueChange = { },
                    label = { Text("End Date") },
                    modifier = Modifier.weight(1f),
                    enabled = !state.isLoading,
                    readOnly = true,
                    trailingIcon = {
                        IconButton(onClick = { showEndDatePicker = true }) {
                            Icon(Icons.Default.CalendarToday, contentDescription = "Select End Date")
                        }
                    }
                )
            }
            
            // Budget
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = state.totalBudget,
                    onValueChange = { viewModel.sendIntent(CreateTripIntent.UpdateTotalBudget(it)) },
                    label = { Text("Total Budget") },
                    modifier = Modifier.weight(1f),
                    enabled = !state.isLoading,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                
                // Currency dropdown
                ExposedDropdownMenuBox(
                    expanded = false,
                    onExpandedChange = { }
                ) {
                    OutlinedTextField(
                        value = state.currency,
                        onValueChange = { viewModel.sendIntent(CreateTripIntent.UpdateCurrency(it)) },
                        readOnly = true,
                        label = { Text("Currency") },
                        modifier = Modifier
                            .menuAnchor()
                            .width(100.dp),
                        enabled = !state.isLoading,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = false) }
                    )
                }
            }
            
            // Cover Image
            Card(
                onClick = onShowImagePicker,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Image,
                        contentDescription = "Add Cover Image",
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (state.coverImageUrl != null) "Change Cover Image" else "Add Cover Image",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
            
            // Notes
            OutlinedTextField(
                value = state.notes,
                onValueChange = { viewModel.sendIntent(CreateTripIntent.UpdateNotes(it)) },
                label = { Text("Notes") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                enabled = !state.isLoading,
                maxLines = 5
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Save Button
            Button(
                onClick = { viewModel.sendIntent(CreateTripIntent.SaveTrip) },
                modifier = Modifier.fillMaxWidth(),
                enabled = !state.isLoading && state.tripName.isNotBlank() && state.destination.isNotBlank()
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Create Trip")
                }
            }
        }
    }
    
    // Date pickers (simplified - in real app would show actual date picker dialogs)
    if (showStartDatePicker) {
        // TODO: Show date picker dialog
        // For now, just increment by one day as example
        viewModel.sendIntent(CreateTripIntent.UpdateStartDate(state.startDate.plusDays(1)))
        showStartDatePicker = false
    }
    
    if (showEndDatePicker) {
        // TODO: Show date picker dialog
        // For now, just increment by one day as example
        viewModel.sendIntent(CreateTripIntent.UpdateEndDate(state.endDate.plusDays(1)))
        showEndDatePicker = false
    }
}

@Preview(showBackground = true)
@Composable
private fun CreateTripScreenPreview() {
    CreateTripScreen(
        onNavigateBack = {},
        onShowDatePicker = {},
        onShowImagePicker = {}
    )
}
