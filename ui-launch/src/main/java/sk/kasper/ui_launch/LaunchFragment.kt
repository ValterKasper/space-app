package sk.kasper.ui_launch


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.asynclayoutinflater.view.AsyncLayoutInflater
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.accompanist.coil.rememberCoilPainter
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.toPaddingValues
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.flow.collect
import sk.kasper.domain.model.LaunchSite
import sk.kasper.space.launchdetail.section.FalconInfoViewModel
import sk.kasper.ui_common.BaseFragment
import sk.kasper.ui_common.theme.SpaceTheme
import sk.kasper.ui_common.utils.*
import sk.kasper.ui_launch.databinding.FragmentLaunchBinding
import sk.kasper.ui_launch.databinding.FragmentLaunchRocketSectionBinding
import sk.kasper.ui_launch.section.*
import timber.log.Timber
import javax.inject.Inject


class LaunchFragment : BaseFragment() {

    @Inject
    lateinit var orbitViewModelFactory: OrbitViewModel.Factory

    @Inject
    lateinit var launchViewModelFactory: LaunchViewModel.Factory

    @Inject
    lateinit var falconInfoViewModelFactory: FalconInfoViewModel.Factory

    @Inject
    lateinit var galleryViewModelFactory: GalleryViewModel.Factory

    @Inject
    lateinit var rocketSectionViewModelFactory: RocketSectionViewModel.Factory

    @Inject
    lateinit var launchSiteViewModelFactory: LaunchSiteViewModel.Factory

    private lateinit var binding: FragmentLaunchBinding

    private val launchViewModel: LaunchViewModel by viewModels {
        launchViewModelFactory.create(getLaunchId())
    }

    private val galleryViewModel: GalleryViewModel by viewModels {
        galleryViewModelFactory.create(getLaunchId())
    }

    private val launchSiteViewModel: LaunchSiteViewModel by viewModels {
        launchSiteViewModelFactory.create(
            getLaunchId(),
            ConnectionResult.SUCCESS == GoogleApiAvailability.getInstance()
                .isGooglePlayServicesAvailable(context)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launchWhenStarted {
            galleryViewModel.sideEffects.collect { se ->
                if (se is ShowPhotoPager) {
                    Toast.makeText(
                        requireContext(),
                        "Show photo pager ${se.photoPagerData.selectedPhotoIndex}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentLaunchBinding.inflate(inflater, container, false)
        binding.tagsView.adapter = TagAdapter()
        binding.tagsView.addItemDecoration(
            HorizontalSpaceItemDecoration(
                R.dimen.launch_tag_horizontal_space.toPixels(
                    requireContext()
                )
            )
        )

        requireActivity().window.statusBarColor =
            ContextCompat.getColor(requireContext(), R.color.systemUiOverlayColor)

        setupLaunchViewModel()
        setupRocketSection()
        setupLaunchSiteViewModel()
        setupGallery()
        setupOrbit()

        NavigationUI.setupWithNavController(binding.toolbar, findNavController())

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()

        requireActivity().window.statusBarColor = android.R.attr.statusBarColor.getThemeColor(requireContext())
    }

    private fun setupGallery() {
        binding.galleryComposeView.setContent {
            SpaceTheme {
                ProvideWindowInsets {
                    Column {
                        val state by galleryViewModel.state.collectAsState()
                        Text(
                            stringResource(id = state.title),
                            style = MaterialTheme.typography.h5,
                            modifier = Modifier
                                .padding(horizontal = 16.dp, vertical = 20.dp)
                                .navigationBarsPadding(bottom = false)
                        )
                        LazyRow(
                            contentPadding = LocalWindowInsets.current.systemBars.toPaddingValues(
                                bottom = false,
                                top = false,
                                additionalHorizontal = 16.dp
                            ),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            items(state.galleryItems) { item ->
                                Surface(
                                    shape = MaterialTheme.shapes.medium,
                                    modifier = Modifier.clickable {
                                        galleryViewModel.submitAction(OnPhotoClicked(item))
                                    }) {
                                    Image(
                                        painter = rememberCoilPainter(item.thumbnailUrl),
                                        contentDescription = item.description,
                                        contentScale = ContentScale.FillHeight,
                                        modifier = Modifier.height(dimensionResource(id = R.dimen.launch_gallery_item_height))
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun getLaunchId() = requireArguments().getString("launchId")!!

    private fun setupLaunchViewModel() {
        binding.viewModel = launchViewModel
        launchViewModel.showVideoUrl.observe(viewLifecycleOwner, Observer {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it))
            startActivity(intent)
        })
    }

    private fun setupOrbit() {
        val viewModel: OrbitViewModel by viewModels { orbitViewModelFactory.create(getLaunchId()) }
        binding.orbitViewModel = viewModel
    }

    private fun setupRocketSection() {
        AsyncLayoutInflater(requireContext()).inflate(R.layout.fragment_launch_rocket_section, binding.sectionsLinearLayout) { view, _, parent ->
            val rocketSectionViewModel: RocketSectionViewModel by viewModels {
                rocketSectionViewModelFactory.create(
                    getLaunchId()
                )
            }
            val falconInfoViewModel: FalconInfoViewModel by viewModels {
                falconInfoViewModelFactory.create(
                    getLaunchId()
                )
            }

            val fragmentLaunchRocketInfoBinding = FragmentLaunchRocketSectionBinding.bind(view)
            fragmentLaunchRocketInfoBinding.rocketSectionViewModel = rocketSectionViewModel
            fragmentLaunchRocketInfoBinding.falconInfoViewModel = falconInfoViewModel
            // to last position because of problem with gallery back animation
            parent?.addView(view, parent.childCount)
        }
    }

    private fun setupLaunchSiteViewModel() {
        // todo is crashing
        // binding.mapView.onCreate(null)
        binding.mapView.getMapAsync { map ->
            MapsInitializer.initialize(context)
            map.uiSettings.isMapToolbarEnabled = false

            val success = map.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                    requireContext(),
                    R.raw.map_style
                )
            )
            if (!success) {
                Timber.e("Style parsing failed.")
            }

            binding.mapView.doOnApplyWindowInsets { insets, _ ->
                map.setPadding((insets.systemWindowInsetLeft + 8f.dp(requireContext())).toInt(), 0, 0, 0)
            }

            observeLaunchSiteViewModel(map, binding)
        }

        binding.launchSiteViewModel = launchSiteViewModel
    }

    private fun observeLaunchSiteViewModel(map: GoogleMap, binding: FragmentLaunchBinding) {
        launchSiteViewModel.launchSite.observe(viewLifecycleOwner, Observer { launchSite: LaunchSite ->
            val position = launchSite.position
            val latLng = LatLng(position.latitude, position.longitude)
            val cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 4f)
            map.moveCamera(cameraUpdate)
            map.addMarker(MarkerOptions()
                    .position(latLng)
                    .title(launchSite.name))

            binding.launchSiteInfo.text = launchSite.name
        })
    }
}