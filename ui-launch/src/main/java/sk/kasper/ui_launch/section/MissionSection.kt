package sk.kasper.ui_launch.section

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import sk.kasper.ui_launch.LaunchViewModel
import sk.kasper.ui_launch.R

@Composable
internal fun MissionSection(viewModel: LaunchViewModel) {
    Column {
        val state by viewModel.state.collectAsState()
        Text(
            text = state.description,
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