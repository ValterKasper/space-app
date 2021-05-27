package sk.kasper.ui_launch.section

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.accompanist.insets.navigationBarsPadding
import sk.kasper.ui_common.theme.section
import sk.kasper.ui_common.view.OrbitView
import sk.kasper.ui_launch.R

@Composable
fun OrbitSection(viewModel: OrbitViewModel) {
    val state by viewModel.state.collectAsState()
    if (state.visible) {
        Column {
            Text(
                stringResource(id = R.string.section_orbit),
                style = MaterialTheme.typography.section,
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 20.dp)
                    .navigationBarsPadding(bottom = false)
            )

            AndroidView(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(184.dp),
                factory = ::OrbitView,
                update = { view -> view.orbit = state.orbit })

            Text(
                text = stringResource(id = state.nameId),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(
                        start = dimensionResource(id = R.dimen.padding_normal),
                        end = dimensionResource(id = R.dimen.padding_normal),
                        top = dimensionResource(id = R.dimen.spacing_normal)
                    ),
                style = MaterialTheme.typography.body1
            )
        }
    }
}