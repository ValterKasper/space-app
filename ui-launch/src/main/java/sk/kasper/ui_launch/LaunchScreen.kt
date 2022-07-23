package sk.kasper.ui_launch

import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.navigationBarsPadding
import sk.kasper.ui_common.theme.SpaceTheme
import sk.kasper.ui_launch.gallery.GallerySection
import sk.kasper.ui_launch.section.*

@Composable
fun LaunchScreen(
    showToast: (String) -> Unit,
    navigateUp: () -> Unit,
    viewData: (uri: Uri) -> Unit
) {
    val galleryViewModel = hiltViewModel<GalleryViewModel>()
    val launchViewModel = hiltViewModel<LaunchViewModel>()
    LaunchedEffect(true) {
        galleryViewModel.sideEffects.collect { sideEffect ->
            if (sideEffect is ShowPhotoPager) {
                showToast("Show photo pager ${sideEffect.photoPagerData.selectedPhotoIndex}")
            }
        }
    }
    LaunchedEffect(true) {
        launchViewModel.sideEffects.collect {
            when (it) {
                is ShowVideo -> {
                    viewData(Uri.parse(it.url))
                }
            }
        }
    }
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
                                viewModel = launchViewModel
                            ) { navigateUp() }
                            MissionSection(viewModel = launchViewModel)
                            GallerySection(viewModel = galleryViewModel)
                            OrbitSection(viewModel = hiltViewModel())
                            RocketSection(rocketSectionViewModel = hiltViewModel())
                            FalconSection(falconInfoViewModel = hiltViewModel())
                            //LaunchSiteSection(launchSiteViewModel)
                        }
                    }
                }
            }
        }
    }
}

