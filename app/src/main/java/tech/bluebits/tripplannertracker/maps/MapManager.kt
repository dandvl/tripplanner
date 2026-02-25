package tech.bluebits.tripplannertracker.maps

import android.content.Context
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*
import com.google.maps.android.clustering.ClusterManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import tech.bluebits.tripplannertracker.data.model.ItineraryItem
import tech.bluebits.tripplannertracker.data.model.VisitedLocation
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MapManager @Inject constructor(
    private val context: Context
) {
    
    private val _mapState = MutableStateFlow<MapState>(MapState.Idle)
    val mapState: Flow<MapState> = _mapState.asStateFlow()
    
    private var googleMap: GoogleMap? = null
    private var clusterManager: ClusterManager<MapMarker>? = null
    
    fun initializeMap(map: GoogleMap) {
        this.googleMap = map
        setupClusterManager(map)
        setupMapSettings(map)
        _mapState.value = MapState.Ready
    }
    
    private fun setupClusterManager(map: GoogleMap) {
        clusterManager = ClusterManager(context, map)
        map.setOnCameraIdleListener(clusterManager)
        map.setOnMarkerClickListener(clusterManager)
    }
    
    private fun setupMapSettings(map: GoogleMap) {
        map.apply {
            uiSettings.apply {
                isZoomControlsEnabled = true
                isCompassEnabled = true
                isMyLocationButtonEnabled = true
                isMapToolbarEnabled = false
            }
            
            try {
                isMyLocationEnabled = true
            } catch (e: SecurityException) {
                // Handle permission exception
            }
        }
    }
    
    fun showItineraryItems(items: List<ItineraryItem>) {
        val markers = items.mapNotNull { item ->
            if (item.latitude != null && item.longitude != null) {
                MapMarker(
                    id = item.id,
                    position = LatLng(item.latitude, item.longitude),
                    title = item.title,
                    snippet = item.description,
                    type = MarkerType.ITINERARY,
                    data = item
                )
            } else null
        }
        
        clusterManager?.addItems(markers)
        clusterManager?.cluster()
        
        if (markers.isNotEmpty()) {
            zoomToMarkers(markers.map { it.position })
        }
    }
    
    fun showVisitedLocations(locations: List<VisitedLocation>) {
        val markers = locations.map { location ->
            MapMarker(
                id = location.id,
                position = LatLng(location.latitude, location.longitude),
                title = location.name,
                snippet = "Visited at ${location.visitedAt}",
                type = MarkerType.VISITED_LOCATION,
                data = location
            )
        }
        
        clusterManager?.addItems(markers)
        clusterManager?.cluster()
        
        if (markers.isNotEmpty()) {
            zoomToMarkers(markers.map { it.position })
        }
    }
    
    fun showRoute(points: List<LatLng>) {
        googleMap?.let { map ->
            val polylineOptions = PolylineOptions().apply {
                addAll(points)
                width(8f)
                color(context.getColor(android.R.color.holo_blue_dark))
                geodesic(true)
            }
            
            map.addPolyline(polylineOptions)
            
            if (points.isNotEmpty()) {
                zoomToMarkers(points)
            }
        }
    }
    
    fun showCurrentLocation(location: LatLng) {
        googleMap?.let { map ->
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 15f))
        }
    }
    
    fun clearMap() {
        googleMap?.clear()
        clusterManager?.clearItems()
    }
    
    private fun zoomToMarkers(positions: List<LatLng>) {
        if (positions.isEmpty()) return
        
        googleMap?.let { map ->
            val boundsBuilder = LatLngBounds.Builder()
            positions.forEach { boundsBuilder.include(it) }
            
            val bounds = boundsBuilder.build()
            val padding = 100 // 100px padding
            val cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding)
            
            map.animateCamera(cameraUpdate)
        }
    }
    
    fun addCustomMarker(
        position: LatLng,
        title: String,
        snippet: String,
        icon: BitmapDescriptor? = null
    ): Marker? {
        return googleMap?.addMarker(
            MarkerOptions()
                .position(position)
                .title(title)
                .snippet(snippet)
                .icon(icon)
        )
    }
    
    fun enableLocationTracking(enabled: Boolean) {
        googleMap?.let { map ->
            try {
                map.isMyLocationEnabled = enabled
            } catch (e: SecurityException) {
                // Handle permission exception
            }
        }
    }
    
    fun setMapType(mapType: Int) {
        googleMap?.mapType = mapType
    }
    
    fun enableTrafficLayer(enabled: Boolean) {
        googleMap?.isTrafficEnabled = enabled
    }
    
    fun enableMyLocationButton(enabled: Boolean) {
        googleMap?.uiSettings?.isMyLocationButtonEnabled = enabled
    }
}

sealed class MapState {
    object Idle : MapState()
    object Ready : MapState()
    object Loading : MapState()
    data class Error(val message: String) : MapState()
}

enum class MarkerType {
    ITINERARY,
    VISITED_LOCATION,
    CURRENT_LOCATION,
    WAYPOINT
}

data class MapMarker(
    val id: String,
    val position: LatLng,
    val title: String,
    val snippet: String,
    val type: MarkerType,
    val data: Any? = null
) : com.google.maps.android.clustering.ClusterItem {
    
    override fun getPosition(): LatLng = position
    override fun getTitle(): String? = title
    override fun getSnippet(): String? = snippet
}
