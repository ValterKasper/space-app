package sk.kasper.ui_common.about

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.navigationBarsPadding
import sk.kasper.ui_common.R
import sk.kasper.ui_common.theme.SpaceTheme
import sk.kasper.ui_common.ui.InsetAwareTopAppBar

private val libraries = listOf(
    Library("Android support libraries", "https://developer.android.com/topic/libraries/support-library"),
    Library("Prettytime", "https://github.com/ocpsoft/prettytime"),
    Library("Dagger 2", "https://google.github.io/dagger/"),
    Library("Timber", "https://github.com/JakeWharton/timber"),
    Library("Threetenabp", "https://github.com/JakeWharton/ThreeTenABP"),
    Library("OkHttp", "http://square.github.io/okhttp/"),
    Library("Retrofit 2", "http://square.github.io/retrofit/")
)

@Composable
fun LibrariesScreen(navigateUp: () -> Unit, viewData: (Uri) -> Unit) {
    SpaceTheme {
        ProvideWindowInsets {
            Column {
                LibrariesToolbar(navigateUp)
                Text(
                    text = stringResource(id = R.string.app_is_built_with_these_libraries),
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier
                        .padding(top = dimensionResource(id = R.dimen.padding_normal))
                        .padding(horizontal = dimensionResource(id = R.dimen.padding_normal))
                        .navigationBarsPadding(bottom = false)
                )
                Libraries(viewData)
            }
        }
    }
}

//    @OptIn(ExperimentalMaterialApi::class)
// TODO D: proper list item
@Composable
private fun Libraries(onLibraryClick: (Uri) -> Unit) {
    LazyColumn {
        items(libraries) { library ->
            Row(modifier = Modifier.clickable { onLibraryClick(Uri.parse(library.link)) }.height(48.dp).fillMaxWidth()) {
                Text(text = library.name)
                Text(text = library.link)
            }
//                ListItem(
//                    modifier = Modifier.clickable { onLibraryClick(library) },
//                    text = { Text(text = library.name) },
//                    secondaryText = { Text(text = library.link) }
//                )
        }
    }
}

@Composable
private fun LibrariesToolbar(navigateUp: () -> Unit) {
    InsetAwareTopAppBar(
        title = {
            Text(
                text = stringResource(id = R.string.libraries),
                style = MaterialTheme.typography.h6,
            )
        },
        navigationIcon = {
            IconButton(onClick = navigateUp) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_back),
                    contentDescription = "back",
                )
            }
        })
}

private data class Library(val name: String, val link: String)

