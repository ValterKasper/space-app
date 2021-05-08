package sk.kasper.ui_playground

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.Bottom
import androidx.compose.ui.Alignment.Companion.Top
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.findNavController
import sk.kasper.ui_common.BaseFragment
import sk.kasper.ui_common.theme.SpaceTheme
import java.util.*

class ComposePlaygroundFragment : BaseFragment() {
    enum class Screen(val text: String) {
        Type("type"),
        Color("color"),
        Components("components"),
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                SpaceTheme {
                    Scaffold(topBar = {
                        TopAppBar(
                            elevation = 0.dp,
                            title = {
                                Text(
                                    text = "Compose playground",
                                    style = MaterialTheme.typography.h6
                                )
                            },
                            navigationIcon = {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_arrow_back),
                                    contentDescription = "back",
                                    tint = MaterialTheme.colors.onPrimary, // todo try better
                                    modifier = Modifier
                                        .clickable(onClick = { findNavController().navigateUp() })
                                        .padding(horizontal = 16.dp)
                                )
                            },
                            actions = {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_tonality),
                                    contentDescription = "Toggle theme",
                                    tint = MaterialTheme.colors.onPrimary,
                                    modifier = Modifier
                                        .clickable(onClick = { findNavController().navigateUp() })
                                        .padding(horizontal = 16.dp)
                                )
                            }
                        )
                    }
                    ) {
                        Column {
                            val selectedScreen: MutableState<Screen> =
                                remember { mutableStateOf(Screen.Components) }
                            TabRow(selectedTabIndex = selectedScreen.value.ordinal) {
                                Screen.values().forEach { screen ->
                                    Tab(
                                        modifier = Modifier.height(56.dp),
                                        selected = selectedScreen.value === screen,
                                        onClick = { selectedScreen.value = screen },
                                    ) {
                                        Text(
                                            style = MaterialTheme.typography.button,
                                            text = screen.text.toUpperCase(Locale.getDefault())
                                        )
                                    }
                                }
                            }
                            LazyColumn {
                                item {
                                    when (selectedScreen.value) {
                                        Screen.Type -> TypeComposable()
                                        Screen.Color -> ColorsComposable()
                                        Screen.Components -> ComponentsComposable()
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun TypeComposable() {
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
                text = name.capitalize(Locale.getDefault()),
                style = textStyle,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }

    @Composable
    fun ColorsComposable() {
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
            val contentColor = if (color in listOf(
                    MaterialTheme.colors.onSecondary,
                    MaterialTheme.colors.onBackground,
                    MaterialTheme.colors.onSurface
                )
            ) MaterialTheme.colors.surface
            else
                contentColorFor(backgroundColor = color)

            Surface(
                modifier = Modifier
                    .height(64.dp)
                    .fillMaxWidth(),
                color = color,
                contentColor = contentColor
            ) {
                Row(modifier = Modifier.padding(8.dp)) {
                    Text(
                        modifier = Modifier
                            .weight(1f)
                            .align(Top),
                        text = name.capitalize(Locale.getDefault()),
                        style = MaterialTheme.typography.body1
                    )
                    Text(
                        modifier = Modifier.align(Bottom),
                        text = "0x" + color.toArgb().toUInt().toString(16).toUpperCase(
                            Locale.getDefault()
                        ),
                        style = MaterialTheme.typography.body2
                    )
                }
            }
        }
    }

    @Composable
    fun ComponentsComposable() {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextButton(onClick = { }) {
                Text(text = "text".toUpperCase(Locale.getDefault()))
            }
            OutlinedButton(onClick = { }) {
                Text(text = "outlined".toUpperCase(Locale.getDefault()))
            }
            Button(onClick = { }) {
                Text(text = "button".toUpperCase(Locale.getDefault()))
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
    }
}