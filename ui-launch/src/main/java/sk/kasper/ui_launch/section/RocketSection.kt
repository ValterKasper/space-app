package sk.kasper.ui_launch.section

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.navigationBarsPadding
import sk.kasper.ui_common.theme.section
import sk.kasper.ui_launch.R

@Composable
fun RocketSection(rocketSectionViewModel: RocketSectionViewModel) {
    val state by rocketSectionViewModel.state.collectAsState()

    if (state.visible) {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .navigationBarsPadding(bottom = false)
        ) {
            Text(
                stringResource(id = state.title),
                style = MaterialTheme.typography.section,
                modifier = Modifier
                    .padding(vertical = 20.dp)
            )

            LabelValue(R.string.rocket_name, state.rocketName)
            LabelValue(R.string.height, state.height)
            LabelValue(R.string.diameter, state.diameter)
            LabelValue(R.string.mass, state.mass)
            LabelValue(R.string.payload_leo, state.payloadLeo)
            LabelValue(R.string.payload_gto, state.payloadGto)
            LabelValue(R.string.thrust, state.thrust)
            LabelValue(R.string.stages, state.stages)
        }
    }
}