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
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.accompanist.coil.rememberCoilPainter
import com.google.accompanist.imageloading.ImageLoadState
import com.google.accompanist.insets.*
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
import sk.kasper.ui_common.tag.UiTag
import sk.kasper.ui_common.theme.SpaceTheme
import sk.kasper.ui_common.theme.section
import sk.kasper.ui_common.ui.LaunchDateTime
import sk.kasper.ui_common.ui.TagsRow
import sk.kasper.ui_common.utils.doOnApplyWindowInsets
import sk.kasper.ui_common.utils.dp
import sk.kasper.ui_common.utils.getThemeColor
import sk.kasper.ui_common.utils.viewModels
import sk.kasper.ui_launch.databinding.FragmentLaunchBinding
import sk.kasper.ui_launch.databinding.FragmentLaunchRocketSectionBinding
import sk.kasper.ui_launch.section.*
import timber.log.Timber
import java.util.*
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

        requireActivity().window.statusBarColor =
            ContextCompat.getColor(requireContext(), R.color.systemUiOverlayColor)

        setupLaunchViewModel()
        setupRocketSection()
        setupLaunchSiteViewModel()
        setupGallery()
        setupOrbit()

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
                    Surface(color = MaterialTheme.colors.background) {
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
    }

    private fun getLaunchId() = requireArguments().getString("launchId")!!

    private fun setupLaunchViewModel() {
        lifecycleScope.launchWhenStarted {
            launchViewModel.sideEffects.collect {
                when (it) {
                    is ShowVideo -> {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it.url))
                        startActivity(intent)
                    }
                }
            }
        }

        binding.composeToolbarToolbarLayout.setContent {
            val state by launchViewModel.state.collectAsState()
            LaunchHeader(
                state = state,
                upClick = { findNavController().navigateUp() },
                onShowVideoClick = { launchViewModel.submitAction(VideoClick) }
            )
        }
    }

    @Preview
    @Composable
    fun LaunchHeaderPreview() {
        LaunchHeader(
            state = LaunchState(
                tags = listOf(UiTag.CUBE_SAT, UiTag.PROBE, UiTag.MARS),
                missionName = "Meteor-M №2-1 Meteor-M №2-1 Meteor-M №2-1"
            )
        )
    }

    @Composable
    fun LaunchHeader(
        state: LaunchState,
        upClick: () -> Unit = {},
        onShowVideoClick: () -> Unit = {}
    ) {
        SpaceTheme(isDarkTheme = true) {
            ProvideWindowInsets {
                val painter = rememberCoilPainter(
                    request = state.mainPhoto,
                    previewPlaceholder = state.mainPhotoFallback,
                    fadeIn = true
                )

                Surface(color = MaterialTheme.colors.background) {
                    Box {
                        HeaderImage(painter)
                        // find better way solution to show fallback photo
                        if (painter.loadState is ImageLoadState.Error) {
                            HeaderImage(painterResource(id = state.mainPhotoFallback))
                        }
                        Column(
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .fillMaxWidth()
                                .background(
                                    brush = Brush.verticalGradient(
                                        colors = listOf(
                                            Color.Transparent,
                                            colorResource(id = R.color.textProtectionGradientStart)
                                        )
                                    )
                                )
                                .padding(dimensionResource(id = R.dimen.padding_normal))
                                .navigationBarsPadding(bottom = false)
                        ) {
                            Text(
                                modifier = Modifier.padding(bottom = 8.dp),
                                text = state.missionName,
                                style = MaterialTheme.typography.section,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                            )

                            LaunchDateTime(
                                launchDateTime = state.launchDateTime,
                                formattedTimeType = state.formattedTimeType,
                                dateConfirmed = state.dateConfirmed,
                                prettyTimeVisible = false,
                                formattedTimeVisible = state.formattedTimeVisible
                            )

                            if (state.tags.isNotEmpty()) {
                                // header has always dark theme but tags follows current light/dark theme
                                SpaceTheme {
                                    TagsRow(Modifier.padding(top = 8.dp), list = state.tags)
                                }
                            }
                        }
                        IconButton(
                            modifier = Modifier
                                .align(Alignment.TopStart)
                                .statusBarsPadding()
                                .navigationBarsPadding(bottom = false),
                            onClick = { upClick() }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_arrow_back),
                                contentDescription = "back",
                            )
                        }

                        if (state.showVideoUrl) {
                            TextButton(
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(
                                        top = 8.dp,
                                        end = dimensionResource(id = R.dimen.padding_normal)
                                    )
                                    .navigationBarsPadding()
                                    .systemBarsPadding(),
                                onClick = { onShowVideoClick() }
                            ) {
                                Text(
                                    text = stringResource(id = R.string.watch_launch_live).toUpperCase(
                                        Locale.getDefault()
                                    ),
                                    color = colorResource(id = R.color.youtube_red)
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun HeaderImage(painter: Painter) {
        Image(
            modifier = Modifier
                .height(260.dp)
                .fillMaxWidth(),
            contentScale = ContentScale.Crop,
            painter = painter,
            contentDescription = stringResource(id = R.string.rocket_photo)
        )
    }

    private fun setupOrbit() {
        val viewModel: OrbitViewModel by viewModels { orbitViewModelFactory.create(getLaunchId()) }
        binding.orbitViewModel = viewModel
    }

    private fun setupRocketSection() {
        AsyncLayoutInflater(requireContext()).inflate(
            R.layout.fragment_launch_rocket_section,
            binding.sectionsLinearLayout
        ) { view, _, parent ->
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