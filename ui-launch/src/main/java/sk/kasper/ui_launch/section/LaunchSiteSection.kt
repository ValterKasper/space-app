package sk.kasper.ui_launch.section

import android.os.Bundle
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.accompanist.insets.navigationBarsPadding
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import sk.kasper.ui_common.theme.section
import sk.kasper.ui_launch.R
import timber.log.Timber

@Composable
fun LaunchSiteSection(launchSiteViewModel: LaunchSiteViewModel) {
    val state by launchSiteViewModel.state.collectAsState()

    if (state.visible) {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .navigationBarsPadding(bottom = false)
        ) {
            Text(
                stringResource(id = state.title),
                style = MaterialTheme.typography.section,
                modifier = Modifier
                    .padding(vertical = 20.dp)
            )

            state.launchSite?.let { launchSite ->
                val current = LocalContext.current
                AndroidView(
                    modifier = Modifier
                        .height(184.dp)
                        .fillMaxWidth(),
                    factory = { context ->
                        MapView(
                            context, GoogleMapOptions()
                                .liteMode(true)
                                .mapToolbarEnabled(false)
                                .mapType(GoogleMap.MAP_TYPE_NORMAL)
                        )
                    },
                    update = { view ->
                        view.onCreate(Bundle.EMPTY)
                        view.getMapAsync { map ->
                            MapsInitializer.initialize(current)

                            val position = launchSite.position
                            val latLng = LatLng(position.latitude, position.longitude)
                            val cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 4f)
                            map.moveCamera(cameraUpdate)
                            map.addMarker(
                                MarkerOptions()
                                    .position(latLng)
                                    .title(launchSite.name)
                            )

                            val success = map.setMapStyle(
                                MapStyleOptions.loadRawResourceStyle(
                                    current,
                                    R.raw.map_style
                                )
                            )

                            if (!success) {
                                Timber.e("Style parsing failed.")
                            }
                        }
                    }
                )

                Text(
                    launchSite.name,
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier
                        .padding(
                            top = dimensionResource(id = R.dimen.spacing_normal)
                        )
                )
            }
        }
    }
}
