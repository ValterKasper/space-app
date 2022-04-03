package sk.kasper.ui_playground

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColor
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Bottom
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.Top
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.findNavController
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.navigationBarsHeight
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.statusBarsPadding
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import sk.kasper.base.SettingsManager
import sk.kasper.ui_common.BaseFragment
import sk.kasper.ui_common.tag.Filter
import sk.kasper.ui_common.tag.FilterDefinition
import sk.kasper.ui_common.theme.SpaceTheme
import sk.kasper.ui_common.ui.InsetAwareTopAppBar
import java.util.*
import javax.inject.Inject
import kotlin.math.roundToInt

@AndroidEntryPoint
class ComposePlaygroundFragment : BaseFragment() {

    @Inject
    lateinit var settingsManager: SettingsManager

    enum class PlaygroundTab(val text: String) {
        TYPE("type"),
        COLOR("color"),
        SHAPE("shape"),
        COMPONENTS("components"),
        ANIMATIONS("anim"),
        FILTER("filter"),
    }

    private val defaultTab = PlaygroundTab.FILTER

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
                                darkIcons = false
                            )
                        }

                        var showInset by remember { mutableStateOf(false) }
                        if (showInset) {
                            InsetsScreen { showInset = !showInset }
                        } else {
                            Scaffold(topBar = { PlaygroundTopAppBar { showInset = !showInset } }) {
                                PlaygroundTabs { screen ->
                                    when (screen) {
                                        PlaygroundTab.TYPE -> TypeScreen()
                                        PlaygroundTab.COLOR -> ColorsScreen()
                                        PlaygroundTab.COMPONENTS -> ComponentsScreen()
                                        PlaygroundTab.SHAPE -> ShapeScreen()
                                        PlaygroundTab.ANIMATIONS -> AnimationsScreen()
                                        PlaygroundTab.FILTER -> Filter(
                                            filterDefinition = FilterDefinition(
                                                topFilterItems = emptyList(),
                                                extensionFilterItems = emptyMap()
                                            )
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

    enum class AnimState {
        A, B
    }

    @OptIn(ExperimentalAnimationApi::class)
    @Composable
    private fun AnimationsScreen() {
        var flag by remember {
            mutableStateOf(true)
        }

        val color by animateColorAsState(if (flag) Color(0xFF03A9F4) else Color(0xFF8BC34A))
        val size by animateDpAsState(targetValue = if (flag) 48.dp else 64.dp)
        var animState by remember {
            mutableStateOf(AnimState.A)
        }

        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(onClick = {
                flag = !flag
                animState = if (animState == AnimState.A) AnimState.B else AnimState.A
            }) {
                Text(text = "Toggle")
            }

            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(color)
            ) {
            }
            Row(modifier = Modifier.height(72.dp)) {
                if (flag) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(MaterialTheme.colors.primary)
                    ) {
                    }
                }
            }

            Row(modifier = Modifier.height(72.dp)) {
                Box(
                    modifier = Modifier
                        .size(size)
                        .background(MaterialTheme.colors.primary)
                ) {
                }
            }

            Row(modifier = Modifier.height(72.dp)) {
                val transition = updateTransition(targetState = animState, label = "aaa")

                val col by transition.animateColor(label = "") {
                    when (it) {
                        AnimState.A -> Color(0xFF9C27B0)
                        AnimState.B -> Color(0xFFFF5722)
                    }
                }
                val siz by transition.animateDp(label = "") {
                    when (it) {
                        AnimState.A -> 32.dp
                        AnimState.B -> 48.dp
                    }
                }

                Box(
                    modifier = Modifier
                        .size(siz)
                        .background(col)
                ) {
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Green)
            ) {
                BoxWithConstraints {
                    Text("My minWidth is $minWidth while my maxWidth is $maxWidth")
                }
            }
        }
    }

    private fun Modifier.placeBehindHorizontal(amount: Float) = layout { measurable, constraints ->
        val placeable = measurable.measure(constraints)

        layout(constraints.maxWidth, placeable.height) {
            val fl: Float = (constraints.maxWidth) * amount
            placeable.place(fl.roundToInt(), 0)
        }
    }

    @Composable
    private fun PlaygroundTabs(onTabSelected: @Composable (PlaygroundTab) -> Unit) {
        Column {
            val selectedPlaygroundTab: MutableState<PlaygroundTab> =
                remember { mutableStateOf(defaultTab) }
            Surface(
                color = MaterialTheme.colors.primarySurface
            ) {
                ScrollableTabRow(
                    backgroundColor = Color.Transparent,
                    modifier = Modifier.navigationBarsPadding(bottom = false),
                    selectedTabIndex = selectedPlaygroundTab.value.ordinal
                ) {
                    PlaygroundTab.values().forEach { screen ->
                        Tab(
                            modifier = Modifier.height(56.dp),
                            selected = selectedPlaygroundTab.value === screen,
                            onClick = { selectedPlaygroundTab.value = screen },
                        ) {
                            Text(
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                style = MaterialTheme.typography.button,
                                text = screen.text.uppercase(Locale.getDefault())
                            )
                        }
                    }
                }
            }
            LazyColumn {
                item {
                    onTabSelected(selectedPlaygroundTab.value)
                }
            }
        }
    }

    @Composable
    private fun ComposeView.PlaygroundTopAppBar(onShowInset: () -> Unit = {}) {
        InsetAwareTopAppBar(
            elevation = 0.dp,
            title = {
                Text(
                    text = "Compose playground",
                    maxLines = 1,
                    style = MaterialTheme.typography.h6,
                    overflow = TextOverflow.Ellipsis,
                )
            },
            navigationIcon = {
                IconButton(onClick = { findNavController().popBackStack() }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_back),
                        contentDescription = "back",
                    )
                }
            },
            actions = {
                IconButton(onClick = { settingsManager.toggleTheme() }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_tonality),
                        contentDescription = "Toggle theme",
                    )
                }

                IconButton(onClick = { onShowInset() }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_live_tv),
                        contentDescription = "Insets",
                    )
                }
            }
        )
    }

    @Composable
    fun TypeScreen() {
        listOf(
            "headline1" to MaterialTheme.typography.h1,
            "headline2" to MaterialTheme.typography.h2,
            "headline3" to MaterialTheme.typography.h3,
            "headline4" to MaterialTheme.typography.h4,
            "headline5" to MaterialTheme.typography.h5,
            "headline6" to MaterialTheme.typography.h6,
            "subtitle1" to MaterialTheme.typography.subtitle1,
            "subtitle2" to MaterialTheme.typography.subtitle2,
            "body1" to MaterialTheme.typography.body1,
            "body2" to MaterialTheme.typography.body2,
            "button" to MaterialTheme.typography.button,
            "caption" to MaterialTheme.typography.caption,
            "overline" to MaterialTheme.typography.overline,
        ).forEach { (name, textStyle) ->
            Text(
                text = name.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() },
                style = textStyle,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }

    @Composable
    fun ColorsScreen() {
        listOf(
            "primary" to MaterialTheme.colors.primary,
            "primaryVariant" to MaterialTheme.colors.primaryVariant,
            "secondary" to MaterialTheme.colors.secondary,
            "secondaryVariant" to MaterialTheme.colors.secondaryVariant,
            "background" to MaterialTheme.colors.background,
            "surface" to MaterialTheme.colors.surface,
            "error" to MaterialTheme.colors.error,
            "onPrimary" to MaterialTheme.colors.onPrimary,
            "onSecondary" to MaterialTheme.colors.onSecondary,
            "onBackground" to MaterialTheme.colors.onBackground,
            "onSurface" to MaterialTheme.colors.onSurface,
            "onError" to MaterialTheme.colors.onError,
        ).forEach { (name, color) ->
            CompositionLocalProvider(LocalContentColor provides Color(0xFF2C9607)) {
                Surface(
                    modifier = Modifier
                        .height(64.dp)
                        .fillMaxWidth(),
                    color = color
                ) {
                    Row(
                        modifier = Modifier
                            .padding(8.dp)
                            .navigationBarsPadding(bottom = false)
                    ) {
                        Text(
                            modifier = Modifier
                                .weight(1f)
                                .align(Top),
                            text = name.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() },
                            style = MaterialTheme.typography.body1
                        )
                        Text(
                            modifier = Modifier.align(Bottom),
                            text = "0x" + color.toArgb().toUInt().toString(16).uppercase(
                                Locale.getDefault()
                            ),
                            style = MaterialTheme.typography.body2
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.navigationBarsHeight())
    }

    @Composable
    fun ComponentsScreen() {
        Column(
            Modifier.navigationBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextButton(onClick = { }) {
                    Text(text = "text".uppercase(Locale.getDefault()))
                }
                OutlinedButton(onClick = { }) {
                    Text(text = "outlined".uppercase(Locale.getDefault()))
                }
                Button(onClick = { }) {
                    Text(text = "button".uppercase(Locale.getDefault()))
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Surface(
                elevation = 4.dp, modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Surface(
                    elevation = 8.dp, modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Surface(
                        elevation = 16.dp, modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .height(32.dp)
                    ) {

                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            ImageBox(
                Modifier.requiredHeight(128.dp),
                contentScale = ContentScale.FillHeight,
                contentDescription = "FillHeight"
            )

            Spacer(modifier = Modifier.height(16.dp))

            ImageBox(
                Modifier
                    .requiredHeight(128.dp)
                    .requiredWidth(128.dp),
                contentScale = ContentScale.Fit,
                contentDescription = "Fit"
            )

            Spacer(modifier = Modifier.height(16.dp))

            ImageBox(
                Modifier
                    .requiredHeight(128.dp)
                    .requiredWidth(128.dp),
                contentScale = ContentScale.Crop,
                contentDescription = "Crop"
            )
        }
    }

    @Composable
    private fun ImageBox(
        modifier: Modifier = Modifier,
        contentScale: ContentScale,
        contentDescription: String
    ) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            modifier = modifier
                .border(2.dp, Color.LightGray)
        ) {
            Box(contentAlignment = BottomCenter) {
                // TODO D: use "https://placebear.com/640/420"
                Image(
                    painter = painterResource(id = R.drawable.atlas_5),
                    contentScale = contentScale,
                    contentDescription = contentDescription
                )
                Text(
                    text = contentDescription,
                    modifier = Modifier.padding(bottom = 8.dp),
                    color = MaterialTheme.colors.onPrimary
                )
            }
        }
    }

    @Composable
    fun ShapeScreen() {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ShapeDemoSurface(MaterialTheme.shapes.small, "Shape small")

            ShapeDemoSurface(
                MaterialTheme.shapes.medium,
                "Shape medium",
                width = 200.dp,
                height = 150.dp
            )

            ShapeDemoSurface(
                MaterialTheme.shapes.large,
                "Shape large",
                width = 300.dp,
                height = 200.dp
            )
        }
    }

    @Composable
    private fun ShapeDemoSurface(
        shape: Shape,
        description: String,
        width: Dp = 0.dp,
        height: Dp = 0.dp
    ) {
        Surface(
            shape = shape,
            elevation = 8.dp,
            modifier = Modifier.padding(16.dp),
            color = MaterialTheme.colors.background
        ) {
            Box(
                modifier = Modifier
                    .requiredWidthIn(min = width)
                    .requiredHeightIn(min = height)
                    .padding(16.dp),
                contentAlignment = Center
            ) {
                Text(text = description, style = MaterialTheme.typography.h5)
            }
        }
    }

    @Composable
    private fun InsetsScreen(onClick: () -> Unit) {
        Surface(modifier = Modifier
            .fillMaxSize()
            .clickable { onClick() }) {

            Box(modifier = Modifier.fillMaxSize()) {
                Box(
                    modifier = Modifier
                        .background(MaterialTheme.colors.surface)
                        .border(4.dp, Color.Blue)
                        .fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .statusBarsPadding()
                            .navigationBarsPadding(bottom = false)
                            .fillMaxWidth()
                            .requiredHeight(72.dp)
                            .background(Color.Green)
                    ) {
                    }
                }

                Box(
                    modifier = Modifier
                        .background(MaterialTheme.colors.surface)
                        .border(4.dp, Color.Blue)
                        .fillMaxWidth()
                        .align(BottomCenter)
                ) {
                    Box(
                        modifier = Modifier
                            .navigationBarsPadding()
                            .fillMaxWidth()
                            .requiredHeight(72.dp)
                            .background(Color.Green)
                    ) {
                    }
                }
            }
        }
    }
}