package sk.kasper.ui_launch


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.dimensionResource
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.navigationBarsPadding
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import kotlinx.coroutines.flow.collect
import sk.kasper.ui_common.BaseFragment
import sk.kasper.ui_common.theme.SpaceTheme
import sk.kasper.ui_common.utils.getThemeColor
import sk.kasper.ui_common.utils.viewModels
import sk.kasper.ui_launch.gallery.GallerySection
import sk.kasper.ui_launch.section.*
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

    private val launchViewModel: LaunchViewModel by viewModels {
        launchViewModelFactory.create(getLaunchId())
    }

    private val galleryViewModel: GalleryViewModel by viewModels {
        galleryViewModelFactory.create(getLaunchId())
    }

    private fun getLaunchId() = requireArguments().getString("launchId")!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launchWhenStarted {
            galleryViewModel.sideEffects.collect { sideEffect ->
                if (sideEffect is ShowPhotoPager) {
                    Toast.makeText(
                        requireContext(),
                        "Show photo pager ${sideEffect.photoPagerData.selectedPhotoIndex}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

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
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        requireActivity().window.statusBarColor =
            ContextCompat.getColor(requireContext(), R.color.systemUiOverlayColor)

        val orbitViewModel: OrbitViewModel by viewModels {
            orbitViewModelFactory.create(
                getLaunchId()
            )
        }

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

        val launchSiteViewModel: LaunchSiteViewModel by viewModels {
            launchSiteViewModelFactory.create(
                getLaunchId(),
                ConnectionResult.SUCCESS == GoogleApiAvailability.getInstance()
                    .isGooglePlayServicesAvailable(context)
            )
        }

        return ComposeView(requireContext()).apply {
            setContent {
                SpaceTheme {
                    ProvideWindowInsets {
                        Surface(color = MaterialTheme.colors.background) {
                            LazyColumn {
                                item {
                                    Column(
                                        modifier = Modifier
                                            .padding(bottom = dimensionResource(id = R.dimen.padding_normal))
                                            .navigationBarsPadding(left = false, right = false)
                                    ) {
                                        HeaderSection(
                                            launchViewModel,
                                            { findNavController().navigateUp() })
                                        GallerySection(galleryViewModel)
                                        OrbitSection(orbitViewModel)
                                        RocketSection(rocketSectionViewModel)
                                        FalconSection(falconInfoViewModel)
                                        LaunchSiteSection(launchSiteViewModel)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        requireActivity().window.statusBarColor =
            android.R.attr.statusBarColor.getThemeColor(requireContext())
    }

}