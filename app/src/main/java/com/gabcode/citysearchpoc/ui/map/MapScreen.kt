package com.gabcode.citysearchpoc.ui.map

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.gabcode.citysearchpoc.domain.City
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberUpdatedMarkerState

@Composable
fun MapScreen(cities: List<City>) {
    val cameraPositionState = rememberCameraPositionState {
        val hqLocation = LatLng(-34.5872182384709, -58.42582240605298)
        position = CameraPosition.fromLatLngZoom(hqLocation, 15f)
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState
    ) {
        cities.forEach { city ->
            Marker(
                state = rememberUpdatedMarkerState(
                    position = LatLng(city.coordinates.latitude, city.coordinates.longitude)
                ),
                title = city.name
            )
        }
    }
}
