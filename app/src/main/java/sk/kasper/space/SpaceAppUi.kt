package sk.kasper.space

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import sk.kasper.ui_common.about.LibrariesScreen
import sk.kasper.ui_common.theme.SpaceTheme
import sk.kasper.ui_common.utils.createSlideAnimNavOptions
import sk.kasper.ui_launch.LaunchScreen
import sk.kasper.ui_playground.ComposePlaygroundScreen
import sk.kasper.ui_settings.preferences.SettingsScreen
import sk.kasper.ui_timeline.ui.TimelineScreen

@Composable
fun SpaceAppUi(viewData: (Uri) -> Unit = { }, showToast: (String) -> Unit = { }) {
    SpaceTheme {
        val navController = rememberNavController()
        NavHost(navController = navController, startDestination = "timeline") {
            val navigate = { uri: String ->
                navController.navigate(uri, createSlideAnimNavOptions())
            }
            val navigateUp: () -> Unit = {
                navController.navigateUp()
            }
            composable("timeline") {
                TimelineScreen(navigate)
            }
            composable("launch/{launchId}") {
                LaunchScreen(showToast = showToast, navigateUp = navigateUp, viewData = viewData)
            }
            composable("settings") {
                SettingsScreen(showToast = showToast, navigate, navigateUp)
            }
            composable("libraries") {
                LibrariesScreen(navigateUp, viewData)
            }
            composable("compose_playground") {
                ComposePlaygroundScreen(navigateUp)
            }
        }
    }
}
