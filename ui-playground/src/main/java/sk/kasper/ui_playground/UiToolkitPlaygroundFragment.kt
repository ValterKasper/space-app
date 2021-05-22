package sk.kasper.ui_playground

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.launch
import sk.kasper.domain.usecase.timeline.GetTimelineItems
import sk.kasper.ui_common.BaseFragment
import sk.kasper.ui_common.settings.SettingsManager
import sk.kasper.ui_playground.databinding.FragmentPlaygroundBinding
import timber.log.Timber
import javax.inject.Inject

class UiToolkitPlaygroundFragment : BaseFragment() {

    @Inject
    lateinit var settingsManager: SettingsManager

    @Inject
    lateinit var getTimelineItems: GetTimelineItems

    private lateinit var binding: FragmentPlaygroundBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlaygroundBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding.toolbar) {
            inflateMenu(R.menu.menu_playground)
            setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.menu_toggle_theme -> {
                        settingsManager.toggleTheme()
                        true
                    }
                    else -> false
                }
            }
            NavigationUI.setupWithNavController(this, findNavController())
        }

        binding.showDemoNotificationButton.setOnClickListener {
            lifecycleScope.launch {
/*
                val timelineItems = getTimelineItems.getTimelineItems(FilterSpec(rockets = setOf(Rocket.FALCON_9)))
                timelineItems.firstOrNull()?.let { rocketLaunch ->
                    NotificationsHelper(requireContext()).showLaunchNotification(LaunchNotificationInfo(
                            rocketLaunch.id,
                            rocketLaunch.rocketId,
                            rocketLaunch.rocketName,
                            rocketLaunch.videoUrl,
                            rocketLaunch.launchName,
                            rocketLaunch.launchDateTime
                    ))
                }
*/
            }
        }

        binding.mapView.onCreate(null)
        binding.mapView.getMapAsync { map ->
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
}
