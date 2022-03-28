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
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.navigationBarsPadding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import sk.kasper.ui_common.BaseFragment
import sk.kasper.ui_common.theme.SpaceTheme
import sk.kasper.ui_common.utils.getThemeColor
import sk.kasper.ui_launch.gallery.GallerySection
import sk.kasper.ui_launch.section.*

@AndroidEntryPoint
class LaunchFragment : BaseFragment() {

    private val launchViewModel: LaunchViewModel by viewModels()

    private val galleryViewModel: GalleryViewModel by viewModels()

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

        val orbitViewModel: OrbitViewModel by viewModels()

        val rocketSectionViewModel: RocketSectionViewModel by viewModels()

        val falconInfoViewModel: FalconInfoViewModel by viewModels()

        val launchSiteViewModel: LaunchSiteViewModel by viewModels()

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
                                            .navigationBarsPadding(start = false, end = false)
                                    ) {
                                        HeaderSection(
                                            launchViewModel
                                        ) { findNavController().navigateUp() }
                                        GallerySection(galleryViewModel)
                                        OrbitSection(orbitViewModel)
                                        RocketSection(rocketSectionViewModel)
                                        FalconSection(falconInfoViewModel)
                                        //LaunchSiteSection(launchSiteViewModel)
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