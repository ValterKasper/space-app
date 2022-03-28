package sk.kasper.ui_launch.gallery

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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.toPaddingValues
import sk.kasper.ui_launch.R
import sk.kasper.ui_launch.section.GalleryViewModel

@Composable
fun GallerySection(viewModel: GalleryViewModel) {
    Column {
        val state by viewModel.state.collectAsState()
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
                        viewModel.onPhotoClicked(item)
                    }) {
                    Image(
                        // TODO D: use item.thumbnailUrl
                        painter = painterResource(id = R.drawable.ariane_5),
                        contentDescription = item.description,
                        contentScale = ContentScale.FillHeight,
                        modifier = Modifier.height(dimensionResource(id = R.dimen.launch_gallery_item_height))
                    )
                }
            }
        }
    }
}