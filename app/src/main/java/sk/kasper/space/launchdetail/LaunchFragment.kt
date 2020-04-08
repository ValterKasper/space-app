package sk.kasper.space.launchdetail


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.asynclayoutinflater.view.AsyncLayoutInflater
import androidx.core.app.SharedElementCallback
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import sk.kasper.domain.model.LaunchSite
import sk.kasper.space.BaseFragment
import sk.kasper.space.R
import sk.kasper.space.databinding.FragmentLaunchBinding
import sk.kasper.space.databinding.FragmentLaunchRocketSectionBinding
import sk.kasper.space.launchdetail.gallery.GalleryAdapter
import sk.kasper.space.launchdetail.section.*
import sk.kasper.space.timeline.TagAdapter
import sk.kasper.space.utils.*
import timber.log.Timber
import javax.inject.Inject


class LaunchFragment : BaseFragment() {

    private var launchId: Long = INVALID_LAUNCH_ID

    private lateinit var launchSiteViewModel: LaunchSiteViewModel
    private lateinit var galleryPositionModel: GalleryPositionModel
    private lateinit var binding: FragmentLaunchBinding

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            launchId = it.getLong(ARG_LAUNCH_ID, INVALID_LAUNCH_ID)
        }

        if (launchId == INVALID_LAUNCH_ID) {
            throw IllegalArgumentException("$ARG_LAUNCH_ID has to be provided")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = FragmentLaunchBinding.inflate(inflater, container, false)
        binding.tagsView.adapter = TagAdapter()
        binding.tagsView.addItemDecoration(HorizontalSpaceItemDecoration(R.dimen.launch_tag_horizontal_space.toPixels(requireContext())))

        requireActivity().window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.systemUiOverlayColor)

        setupLaunchViewModel()
        setupRocketSection()
        setupLaunchSiteViewModel()
        setupGallery()
        setupOrbit()

        binding.toolbar.setNavigationOnClickListener {
            fragmentManager?.popBackStack()
        }

        // todo asi zmaz
        // chcem oznamit VM kolko riadkov je v popise, ale az po layoute a odpovedi z repa
        binding.description.postDelayed({
            binding.viewModel?.onDescriptionLinesCountChanged(binding.description.lineCount)
        }, 100)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        scrollToPosition()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        requireActivity().window.statusBarColor = android.R.attr.statusBarColor.getThemeColor(requireContext())
    }

    override fun onResume() {
        super.onResume()

        view?.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
    }

    private fun scrollToPosition() {
        binding.galleryRecyclerView.addOnLayoutChangeListener(object : View.OnLayoutChangeListener {
            override fun onLayoutChange(v: View,
                                        left: Int,
                                        top: Int,
                                        right: Int,
                                        bottom: Int,
                                        oldLeft: Int,
                                        oldTop: Int,
                                        oldRight: Int,
                                        oldBottom: Int) {
                binding.galleryRecyclerView.removeOnLayoutChangeListener(this)
                binding.galleryRecyclerView.layoutManager?.let { layoutManager ->
                    val viewAtPosition = layoutManager.findViewByPosition(galleryPositionModel.position)
                    // Scroll to position if the view for the current position is null (not currently part of
                    // layout manager children), or it's not completely visible.
                    if (viewAtPosition == null || layoutManager
                                    .isViewPartiallyVisible(viewAtPosition, false, true)) {
                        binding.galleryRecyclerView.post { layoutManager.scrollToPosition(galleryPositionModel.position) }
                    }
                }
            }
        })
    }

    private fun prepareTransitions() {
        exitTransition = TransitionInflater.from(context)
                .inflateTransition(R.transition.grid_exit_transition)

        // A similar mapping is set at the PhotoPagerFragment with a setEnterSharedElementCallback.
        setExitSharedElementCallback(
                object : SharedElementCallback() {
                    override fun onMapSharedElements(names: List<String>?, sharedElements: MutableMap<String, View>?) {
                        // Locate the ViewHolder for the clicked position.
                        val selectedViewHolder = binding.galleryRecyclerView
                                .findViewHolderForAdapterPosition(galleryPositionModel.position)
                        if (selectedViewHolder?.itemView == null) {
                            return
                        }

                        // Map the first shared element name to the child ImageView.
                        sharedElements!![names!![0]] = selectedViewHolder.itemView.findViewById(R.id.galleryImageView)
                    }
                })
    }

    private fun setupGallery() {
        galleryPositionModel = provideViewModelActivityScoped()
        val galleryAdapter = GalleryAdapter(this, galleryPositionModel)
        binding.galleryRecyclerView.adapter = galleryAdapter
        binding.galleryRecyclerView.isNestedScrollingEnabled = false // enables toolbar collapsing when sliding vertically on this view

        val viewModel: GalleryViewModel = provideViewModel { galleryViewModelFactory.create(launchId) }
        viewModel.galleryItems.observe(this, Observer {
            galleryAdapter.setItems(it)
        })

        binding.galleryViewModel = viewModel

        prepareTransitions()
        postponeEnterTransition()
    }

    private fun setupLaunchViewModel() {
        val viewModel: LaunchViewModel = provideViewModel { launchViewModelFactory.create(launchId) }
        binding.viewModel = viewModel
        viewModel.showVideoUrl.observe(this, Observer {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it))
            startActivity(intent)
        })
    }

    private fun setupOrbit() {
        binding.orbitViewModel = provideViewModel { orbitViewModelFactory.create(launchId) }
    }

    private fun setupRocketSection() {
        AsyncLayoutInflater(requireContext()).inflate(R.layout.fragment_launch_rocket_section, binding.sectionsLinearLayout) { view, _, parent ->
            val rocketSectionViewModel: RocketSectionViewModel = provideViewModel { rocketSectionViewModelFactory.create(launchId) }

            val falconInfoViewModel: FalconInfoViewModel = provideViewModel { falconInfoViewModelFactory.create(launchId) }

            val fragmentLaunchRocketInfoBinding = FragmentLaunchRocketSectionBinding.bind(view)
            fragmentLaunchRocketInfoBinding.rocketSectionViewModel = rocketSectionViewModel
            fragmentLaunchRocketInfoBinding.falconInfoViewModel = falconInfoViewModel
            // to last position because of problem with gallery back animation
            parent?.addView(view, parent.childCount)
        }
    }

    private fun setupLaunchSiteViewModel() {
        launchSiteViewModel = provideViewModel { launchSiteViewModelFactory.create(
                launchId,
                ConnectionResult.SUCCESS == GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(context))
        }

        binding.mapView.onCreate(null)
        binding.mapView.getMapAsync { map ->
            MapsInitializer.initialize(context)
            map.uiSettings.isMapToolbarEnabled = false

            val success = map.setMapStyle(MapStyleOptions.loadRawResourceStyle(requireContext(), R.raw.map_style))
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
        launchSiteViewModel.launchSite.observe(this, Observer { launchSite: LaunchSite ->
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

    companion object {
        private const val ARG_LAUNCH_ID = "arg-launch-id"
        private const val INVALID_LAUNCH_ID = -1L

        fun newInstance(launchId: Long): LaunchFragment {
            val fragment = LaunchFragment()
            val args = Bundle()
            args.putLong(ARG_LAUNCH_ID, launchId)
            fragment.arguments = args
            return fragment
        }
    }

}