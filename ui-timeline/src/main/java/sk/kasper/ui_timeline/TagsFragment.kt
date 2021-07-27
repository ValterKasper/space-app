package sk.kasper.ui_timeline

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.statusBarsPadding
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import org.threeten.bp.LocalDateTime
import sk.kasper.ui_common.BaseFragment
import sk.kasper.ui_common.tag.Filter
import sk.kasper.ui_common.theme.SpaceTheme
import sk.kasper.ui_timeline.ui.LaunchListItemLayout
import sk.kasper.ui_timeline.ui.filterDefinition
import timber.log.Timber

@AndroidEntryPoint
class TagsFragment : BaseFragment() {

    companion object {
        val apollo_11 = LaunchListItem(
            "id",
            "Saturn V | Apollo 11",
            LocalDateTime.of(1969, 7, 16, 10, 59),
            rocketResId = R.drawable.saturn_v,
            rocketName = "Saturn V",
            accurateDate = true,
            accurateTime = false,
            tags = emptyList()
        )

        val apollo_12 = LaunchListItem(
            "id",
            "Saturn V | Apollo 12",
            LocalDateTime.of(1969, 11, 14, 10, 59),
            rocketResId = R.drawable.saturn_v,
            rocketName = "Saturn V",
            accurateDate = true,
            accurateTime = false,
            tags = emptyList()
        )

        val apollo_13 = LaunchListItem(
            "id",
            "Saturn V | Apollo 13",
            LocalDateTime.of(1970, 4, 11, 10, 59),
            rocketResId = R.drawable.saturn_v,
            rocketName = "Saturn V",
            accurateDate = true,
            accurateTime = false,
            tags = emptyList()
        )

        val voyager_1 = LaunchListItem(
            "id",
            "Titan III | Voyager 1",
            LocalDateTime.of(1977, 9, 5, 10, 59),
            rocketResId = 0,
            rocketName = "Titan III",
            accurateDate = true,
            accurateTime = false,
            tags = emptyList()
        )


        val shuttle_STS_115 = LaunchListItem(
            "id",
            "Atlantis | STS-115",
            LocalDateTime.of(2006, 9, 9, 10, 59),
            rocketResId = R.drawable.space_shuttle,
            rocketName = "Space Shuttle",
            accurateDate = true,
            accurateTime = false,
            tags = emptyList()
        )

        val shuttle_STS_131 = LaunchListItem(
            "id",
            "Discovery | STS-131",
            LocalDateTime.of(2010, 4, 5, 10, 59),
            rocketResId = R.drawable.space_shuttle,
            rocketName = "Space Shuttle",
            accurateDate = true,
            accurateTime = false,
            tags = emptyList()
        )

        val curiosity = LaunchListItem(
            "id",
            "Atlas V | Curiosity",
            LocalDateTime.of(2011, 11, 26, 10, 59),
            rocketResId = R.drawable.atlas_5,
            rocketName = "Atlas V",
            accurateDate = true,
            accurateTime = false,
            tags = emptyList()
        )


        val falcon_heavy = LaunchListItem(
            "id",
            "Falcon heavy | Maiden flight",
            LocalDateTime.of(2018, 2, 6, 10, 59),
            rocketResId = R.drawable.falcon_heavy,
            rocketName = "Falcon 9",
            accurateDate = true,
            accurateTime = false,
            tags = emptyList()
        )


        val soyuz = LaunchListItem( // ISS
            "id",
            "Soyuz | MS-13",
            LocalDateTime.of(2019, 7, 20, 10, 59),
            rocketResId = R.drawable.soyuz,
            rocketName = "Soyuz",
            accurateDate = true,
            accurateTime = true,
            tags = emptyList()
        )

        val perseverance = LaunchListItem(
            "id",
            "Atlas V | Perseverance",
            LocalDateTime.of(2020, 7, 30, 10, 59),
            rocketResId = R.drawable.atlas_5,
            rocketName = "Atlas V",
            accurateDate = true,
            accurateTime = false,
            tags = emptyList()
        )


        val falcon_crew_2 = LaunchListItem(
            "id",
            "Falcon 9 | Crew-2",
            LocalDateTime.of(2021, 4, 23, 10, 59),
            rocketResId = R.drawable.falcon_9,
            rocketName = "Falcon 9",
            accurateDate = true,
            accurateTime = false,
            tags = emptyList()
        )

        val atlas_5_geo = LaunchListItem(
            "id",
            "Atlas V 421 | SBIRS GEO-5",
            LocalDateTime.of(2021, 7, 25, 10, 59),
            rocketResId = R.drawable.ariane_5,
            rocketName = "Atlas V",
            accurateDate = true,
            accurateTime = true,
            tags = emptyList()
        )


        val all = listOf(
            apollo_11 to true,
            apollo_12 to true,
            apollo_13 to true,
            voyager_1 to true,
            shuttle_STS_115 to true,
            shuttle_STS_131 to true,
            curiosity to true,
            falcon_heavy to true,
            soyuz to true,
            perseverance to true,
            falcon_crew_2 to true,
            atlas_5_geo to true,
        )
    }


    enum class ListState(val list: List<Pair<LaunchListItem, Boolean>>) {

        START(all),

        ISS(
            listOf(
                apollo_11 to false,
                apollo_12 to false,
                apollo_13 to false,
                voyager_1 to false,
                shuttle_STS_115 to true,
                shuttle_STS_131 to true,
                curiosity to false,
                falcon_heavy to false,
                soyuz to true,
                perseverance to false,
                falcon_crew_2 to true,
                atlas_5_geo to false,
            )
        ),

        ISS_SHUTTLE(
            listOf(
                apollo_11 to false,
                apollo_12 to false,
                apollo_13 to false,
                voyager_1 to false,
                shuttle_STS_115 to true,
                shuttle_STS_131 to true,
                curiosity to false,
                falcon_heavy to false,
                soyuz to false,
                perseverance to false,
                falcon_crew_2 to false,
                atlas_5_geo to false,
            )
        ),

        ISS_SHUTTLE_CLEAR(all),

        MARS(
            listOf(
                apollo_11 to false,
                apollo_12 to false,
                apollo_13 to false,
                voyager_1 to false,
                shuttle_STS_115 to false,
                shuttle_STS_131 to false,
                curiosity to true,
                falcon_heavy to false,
                soyuz to false,
                perseverance to true,
                falcon_crew_2 to false,
                atlas_5_geo to false,
            )
        ),

        MARS_CLEAR(all),

        SPACE_X(
            listOf(
                apollo_11 to false,
                apollo_12 to false,
                apollo_13 to false,
                voyager_1 to false,
                shuttle_STS_115 to false,
                shuttle_STS_131 to false,
                curiosity to false,
                falcon_heavy to true,
                soyuz to false,
                perseverance to false,
                falcon_crew_2 to true,
                atlas_5_geo to false,
            )
        ),

        SPACE_X_F9(
            listOf(
                apollo_11 to false,
                apollo_12 to false,
                apollo_13 to false,
                voyager_1 to false,
                shuttle_STS_115 to false,
                shuttle_STS_131 to false,
                curiosity to false,
                falcon_heavy to false,
                soyuz to false,
                perseverance to false,
                falcon_crew_2 to true,
                atlas_5_geo to false,
            )
        ),
    }


    fun getNextState(ordinal: Int) =
        ListState.values()[(ordinal + 1) % ListState.values().size]

    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                SpaceTheme {
                    ProvideWindowInsets {
                        val systemUiController = rememberSystemUiController()
                        SideEffect {
                            systemUiController.setSystemBarsColor(
                                color = Color.Transparent,
                                darkIcons = true
                            )
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .statusBarsPadding()
                        ) {

                            var listState by remember { mutableStateOf(ListState.START) }

                            Header {
                                val ordinal = listState.ordinal
                                listState = getNextState(ordinal)
                            }

                            LazyColumn(modifier = Modifier.padding(top = 108.dp)) {
                                val currentTime = LocalDateTime.of(2020, 2, 28, 12, 30)

                                items(listState.list) { item ->
                                    AnimatedVisibility(visible = item.second) {
                                        LaunchListItemLayout(
                                            LaunchListItemViewModel(item.first, currentTime)
                                        )
                                    }
                                }
                            }

                            Text(
                                text = "Next: " + getNextState(listState.ordinal),
                                modifier = Modifier.padding(top = 500.dp),
                            )
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun Header(function: () -> Unit) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                val color = Color(0xFF464646)
                Icon(
                    modifier = Modifier.padding(16.dp),
                    tint = color,
                    painter = painterResource(id = R.drawable.ic_arrow_back),
                    contentDescription = "back",
                )

                Text(
                    modifier = Modifier
                        .weight(1f)
                        .padding(bottom = 2.dp),
                    text = "Launches",
                    color = color,
                    maxLines = 1,
                    style = MaterialTheme.typography.h6,
                    overflow = TextOverflow.Ellipsis,
                )

                Icon(
                    modifier = Modifier.padding(16.dp),
                    tint = color,
                    painter = painterResource(id = R.drawable.ic_baseline_more_vert_24),
                    contentDescription = "Insets",
                )
            }

            Filter(onItemSelected = { tag, selected ->
                Timber.d("onTagSelected($tag, $selected)")
                function()
            }, {
                Timber.d("onClearAll")
            }, filterDefinition)
        }
    }
}