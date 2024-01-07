package g58089.mobg5.stible.ui.screens

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.imageResource
import com.mapbox.bindgen.Value
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraBoundsOptions
import com.mapbox.maps.CoordinateBounds
import com.mapbox.maps.MapboxExperimental
import com.mapbox.maps.Style
import com.mapbox.maps.extension.compose.MapEffect
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import com.mapbox.maps.extension.style.layers.properties.generated.IconAnchor
import com.mapbox.maps.extension.style.layers.properties.generated.TextAnchor
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager
import g58089.mobg5.stible.R
import g58089.mobg5.stible.data.dto.Stop

private object ViewportDefaults {
    const val INITIAL_ZOOM = 11.5
    const val INITIAL_PITCH = 0.0
    val BRUSSELS_CENTER: Point = Point.fromLngLat(4.34878, 50.85045) // Brussels center
    const val MIN_ZOOM = 10.5
    val STIB_COORDINATES_BOUNDS = CoordinateBounds(
        Point.fromLngLat(4.26, 50.77),
        Point.fromLngLat(4.52, 50.93),
        false
    )
}

@OptIn(MapboxExperimental::class)
@Composable
fun MapWithStopsPoints(stops: List<Stop>, modifier: Modifier = Modifier) {
    // could be generalized, but YAGNI

    Surface(modifier) {
        val mapViewportState = rememberMapViewportState {
            setCameraOptions {
                zoom(ViewportDefaults.INITIAL_ZOOM)
                pitch(ViewportDefaults.INITIAL_PITCH)
                center(ViewportDefaults.BRUSSELS_CENTER)
            }
        }
        MapboxMap(mapViewportState = mapViewportState) {
            val darkTheme = isSystemInDarkTheme()

            val markerImage = ImageBitmap.imageResource(R.drawable.red_marker).asAndroidBitmap()
            val textIconOffset = listOf(0.0, -2.0)
            val textIconSize = MaterialTheme.typography.labelLarge.fontSize.value.toDouble()
            val textIconColor = MaterialTheme.colorScheme.onSurface.toArgb()
            val textIconHalo = MaterialTheme.colorScheme.surfaceVariant.toArgb()
            val textIconHaloStrength = 1.0

            MapEffect(Unit) { mapView ->
                val cameraBoundsOptions = CameraBoundsOptions.Builder()
                    .bounds(ViewportDefaults.STIB_COORDINATES_BOUNDS)
                    .minZoom(ViewportDefaults.MIN_ZOOM)
                    .build()

                val mapboxMap = mapView.mapboxMap

                mapboxMap.setBounds(cameraBoundsOptions)
                mapboxMap.loadStyle(if (darkTheme) Style.DARK else Style.STANDARD) { style ->
                    style.setStyleImportConfigProperty(
                        "basemap",
                        "showTransitLabels",
                        Value.valueOf(false) // do not display public transport. it would be too easy
                    )
                }
            }



            MapEffect(stops) { mapView ->
                val annotationApi = mapView.annotations
                val pointAnnotationManager = annotationApi.createPointAnnotationManager()

                stops.forEach { stop ->
                    val point = PointAnnotationOptions()
                        .withPoint(Point.fromLngLat(stop.longitude, stop.latitude))
                        .withIconImage(markerImage)
                        .withIconAnchor(IconAnchor.BOTTOM)
                        .withTextField(stop.name)
                        .withTextAnchor(TextAnchor.BOTTOM)
                        .withTextOffset(textIconOffset)
                        .withTextSize(textIconSize)
                        .withTextColor(textIconColor)
                        .withTextHaloColor(textIconHalo)
                        .withTextHaloWidth(textIconHaloStrength)
                    pointAnnotationManager.create(point)
                }
            }
        }
    }


}