package sk.kasper.ui_common.about

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.findNavController
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.navigationBarsPadding
import dagger.hilt.android.AndroidEntryPoint
import sk.kasper.ui_common.BaseFragment
import sk.kasper.ui_common.R
import sk.kasper.ui_common.theme.SpaceTheme
import sk.kasper.ui_common.ui.InsetAwareTopAppBar

@AndroidEntryPoint
class LibrariesFragment: BaseFragment() {

    data class Library(val name: String, val link: String)

    private val libraries = listOf(
        Library(
            "Android support libraries",
            "https://developer.android.com/topic/libraries/support-library"
        ),
        Library("Prettytime", "https://github.com/ocpsoft/prettytime"),
        Library("Dagger 2", "https://google.github.io/dagger/"),
        Library("Timber", "https://github.com/JakeWharton/timber"),
        Library("Threetenabp", "https://github.com/JakeWharton/ThreeTenABP"),
        Library("OkHttp", "http://square.github.io/okhttp/"),
        Library("Retrofit 2", "http://square.github.io/retrofit/")
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                SpaceTheme {
                    ProvideWindowInsets {
                        Column {
                            LibrariesToolbar { findNavController().popBackStack() }
                            Text(
                                text = stringResource(id = R.string.app_is_built_with_these_libraries),
                                style = MaterialTheme.typography.body1,
                                modifier = Modifier
                                    .padding(top = dimensionResource(id = R.dimen.padding_normal))
                                    .padding(horizontal = dimensionResource(id = R.dimen.padding_normal))
                                    .navigationBarsPadding(bottom = false)
                            )
                            Libraries { library -> openLink(library.link) }
                        }
                    }
                }
            }
        }
    }

//    @OptIn(ExperimentalMaterialApi::class)
    // TODO D: proper list item
    @Composable
    private fun Libraries(onLibraryClick: (Library) -> Unit) {
        LazyColumn {
            items(libraries) { library ->
                Row(modifier = Modifier.clickable { onLibraryClick(library) }.height(48.dp).fillMaxWidth()) {
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
    private fun LibrariesToolbar(upClick: () -> Unit) {
        InsetAwareTopAppBar(
            title = {
                Text(
                    text = stringResource(id = R.string.libraries),
                    style = MaterialTheme.typography.h6,
                )
            },
            navigationIcon = {
                IconButton(onClick = upClick) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_back),
                        contentDescription = "back",
                    )
                }
            })
    }

    private fun openLink(link: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(link)
        requireContext().startActivity(intent)
    }

}