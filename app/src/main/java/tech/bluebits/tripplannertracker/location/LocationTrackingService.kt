package tech.bluebits.tripplannertracker.location

import android.Manifest
import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.IBinder
import android.os.Looper
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import tech.bluebits.tripplannertracker.R
import tech.bluebits.tripplannertracker.data.repository.VisitedLocationRepository
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID
import javax.inject.Inject

@AndroidEntryPoint
class LocationTrackingService : Service() {
    
    @Inject
    lateinit var visitedLocationRepository: VisitedLocationRepository
    
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    
    private val serviceScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private val locationFlow = MutableSharedFlow<Location>()
    
    private var currentTripId: String? = null
    private var isTracking = false
    
    companion object {
        const val ACTION_START_TRACKING = "ACTION_START_TRACKING"
        const val ACTION_STOP_TRACKING = "ACTION_STOP_TRACKING"
        const val EXTRA_TRIP_ID = "EXTRA_TRIP_ID"
        
        const val NOTIFICATION_CHANNEL_ID = "location_tracking_channel"
        const val NOTIFICATION_ID = 1234
        
        fun startTracking(context: Context, tripId: String) {
            val intent = Intent(context, LocationTrackingService::class.java).apply {
                action = ACTION_START_TRACKING
                putExtra(EXTRA_TRIP_ID, tripId)
            }
            ContextCompat.startForegroundService(context, intent)
        }
        
        fun stopTracking(context: Context) {
            val intent = Intent(context, LocationTrackingService::class.java).apply {
                action = ACTION_STOP_TRACKING
            }
            ContextCompat.startForegroundService(context, intent)
        }
    }
    
    override fun onCreate() {
        super.onCreate()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        createLocationCallback()
        createNotificationChannel()
    }
    
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START_TRACKING -> {
                currentTripId = intent.getStringExtra(EXTRA_TRIP_ID)
                startLocationTracking()
            }
            ACTION_STOP_TRACKING -> {
                stopLocationTracking()
            }
        }
        return START_STICKY
    }
    
    override fun onBind(intent: Intent?): IBinder? = null
    
    private fun createLocationCallback() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.lastLocation?.let { location ->
                    serviceScope.launch {
                        locationFlow.emit(location)
                        saveVisitedLocation(location)
                    }
                }
            }
        }
    }
    
    @SuppressLint("MissingPermission")
    private fun startLocationTracking() {
        if (!hasLocationPermissions()) {
            stopSelf()
            return
        }
        
        if (isTracking) return
        
        isTracking = true
        
        val locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            30000L // 30 seconds
        ).apply {
            setMinUpdateIntervalMillis(15000L) // 15 seconds
            setMaxUpdateDelayMillis(60000L) // 1 minute
        }.build()
        
        try {
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
            
            startForeground(NOTIFICATION_ID, createNotification())
        } catch (e: Exception) {
            isTracking = false
            stopSelf()
        }
    }
    
    private fun stopLocationTracking() {
        if (!isTracking) return
        
        isTracking = false
        
        try {
            fusedLocationClient.removeLocationUpdates(locationCallback)
        } catch (e: Exception) {
            // Ignore
        }
        
        stopForeground(true)
        stopSelf()
    }
    
    private fun saveVisitedLocation(location: Location) {
        currentTripId?.let { tripId ->
            serviceScope.launch {
                try {
                    val visitedLocation = tech.bluebits.tripplannertracker.data.model.VisitedLocation(
                        id = UUID.randomUUID().toString(),
                        tripId = tripId,
                        name = "Location ${System.currentTimeMillis()}",
                        latitude = location.latitude,
                        longitude = location.longitude,
                        visitedAt = LocalDateTime.now(),
                        isManual = false
                    )
                    
                    visitedLocationRepository.insertVisitedLocation(visitedLocation)
                } catch (e: Exception) {
                    // Handle error
                }
            }
        }
    }
    
    private fun hasLocationPermissions(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }
    
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                "Location Tracking",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Tracks your location during trips"
            }
            
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }
    
    private fun createNotification(): Notification {
        val intent = Intent(this, LocationTrackingService::class.java).apply {
            action = ACTION_STOP_TRACKING
        }
        val pendingIntent = PendingIntent.getService(
            this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        return NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setContentTitle("Trip Tracking Active")
            .setContentText("Your location is being tracked for your trip")
            .setSmallIcon(R.drawable.ic_launcher_foreground) // Replace with actual icon
            .addAction(
                R.drawable.ic_launcher_foreground, // Replace with actual icon
                "Stop Tracking",
                pendingIntent
            )
            .setOngoing(true)
            .build()
    }
    
    override fun onDestroy() {
        super.onDestroy()
        stopLocationTracking()
        serviceScope.cancel()
    }
}
