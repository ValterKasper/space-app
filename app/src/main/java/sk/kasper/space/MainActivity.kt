package sk.kasper.space

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import sk.kasper.ui_common.about.LibrariesScreen
import sk.kasper.ui_common.theme.SpaceTheme
import sk.kasper.ui_common.utils.backpress.BackPressManager
import sk.kasper.ui_common.utils.createSlideAnimNavOptions
import sk.kasper.ui_launch.LaunchScreen
import sk.kasper.ui_playground.ComposePlaygroundScreen
import sk.kasper.ui_settings.preferences.SettingsScreen
import sk.kasper.ui_timeline.ui.TimelineScreen
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var backPressManager: BackPressManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SpaceTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "timeline") {
                    val navigate = { uri: String ->
                        navController.navigate(uri, createSlideAnimNavOptions())
                    }
                    val showToast: (String) -> Unit = {
                        Toast.makeText(
                            this@MainActivity,
                            it,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    val navigateUp: () -> Unit = {
                        navController.navigateUp()
                    }
                    val viewData = { uri: Uri ->
                        val intent = Intent(Intent.ACTION_VIEW, uri)
                        startActivity(intent)
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

        // request to be laid out fullscreen
        WindowCompat.setDecorFitsSystemWindows(window, false)
    }

    override fun onBackPressed() {
        if (!backPressManager.onBackPress()) {
            super.onBackPressed()
        }
    }

}
