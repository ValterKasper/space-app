package sk.kasper.space.playground

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import sk.kasper.space.BaseFragment
import sk.kasper.space.R
import sk.kasper.space.databinding.FragmentPlaygroundBinding
import sk.kasper.space.settings.SettingsManager
import sk.kasper.space.view.TopToolbar
import timber.log.Timber
import javax.inject.Inject

// todo mozno pridaj zmenu temy (shapeapperance, farba)
// todo UI prvky
class PlaygroundFragment : BaseFragment() {

    @Inject
    lateinit var settingsManager: SettingsManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return FragmentPlaygroundBinding.inflate(inflater, container, false).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(view.findViewById<TopToolbar>(R.id.toolbar)) {
            inflateMenu(R.menu.menu_playground)
            setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.menu_toggle_theme -> {
                        toggleTheme()
                        true
                    }
                    else -> false
                }
            }
            setNavigationOnClickListener {
                fragmentManager?.popBackStack()
            }
        }

        val mapView = view.findViewById<MapView>(R.id.mapView)
        mapView.onCreate(null)
        mapView.getMapAsync { map ->
            MapsInitializer.initialize(context)
            map.uiSettings.isMapToolbarEnabled = false

            val latLng = LatLng(34.632, -120.611)
            val cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 4f)
            map.moveCamera(cameraUpdate)
            map.addMarker(MarkerOptions()
                    .position(latLng)
                    .title("Vandenberg"))

            val success = map.setMapStyle(MapStyleOptions.loadRawResourceStyle(requireContext(), R.raw.map_style))
            if (!success) {
                Timber.e("Style parsing failed.")
            }
        }
    }

    override fun onResume() {
        super.onResume()

        view?.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
    }

    private fun toggleTheme() {
        settingsManager.nightMode = if (settingsManager.nightMode == AppCompatDelegate.MODE_NIGHT_NO) {
            AppCompatDelegate.MODE_NIGHT_YES
        } else {
            AppCompatDelegate.MODE_NIGHT_NO
        }
    }
}
