package sk.kasper.ui_launch

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.statusBarsPadding
import com.google.accompanist.insets.systemBarsPadding
import sk.kasper.ui_common.tag.FilterTag
import sk.kasper.ui_common.theme.SpaceTheme
import sk.kasper.ui_common.theme.section
import sk.kasper.ui_common.ui.LaunchDateTime
import sk.kasper.ui_common.ui.TagsRow
import java.util.*

@Composable
fun HeaderSection(viewModel: LaunchViewModel, upClick: () -> Unit) {
    val state by viewModel.state.collectAsState()
    LaunchHeader(
        state = state,
        upClick = { upClick() },
        onShowVideoClick = { viewModel.onVideoClick() }
    )
}


@Composable
private fun LaunchHeader(
    state: LaunchState,
    upClick: () -> Unit = {},
    onShowVideoClick: () -> Unit = {}
) {
    SpaceTheme(isDarkTheme = true) {
        Surface(color = MaterialTheme.colors.background) {
            Box {
                // TODO D: use state.mainPhoto
                HeaderImage(painterResource(id = state.mainPhotoFallback))
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

@Composable
private fun HeaderImage(painter: Painter) {
    Image(
        modifier = Modifier
            .height(260.dp)
            .fillMaxWidth(),
        contentScale = ContentScale.Crop,
        painter = painter,
        contentDescription = stringResource(id = R.string.rocket_photo)
    )
}

@Preview
@Composable
fun LaunchHeaderPreview() {
    LaunchHeader(
        state = LaunchState(
            tags = listOf(FilterTag.CUBE_SAT, FilterTag.PROBE, FilterTag.MARS),
            missionName = "Meteor-M №2-1 Meteor-M №2-1 Meteor-M №2-1"
        )
    )
}