package sk.kasper.ui_launch.section

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.navigationBarsPadding
import sk.kasper.ui_common.theme.section
import sk.kasper.ui_launch.R

@Composable
fun FalconSection(falconInfoViewModel: FalconInfoViewModel) {
    val state by falconInfoViewModel.state.collectAsState()

    if (state.visible) {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .navigationBarsPadding(bottom = false)
        ) {
            Text(
                stringResource(id = R.string.falcon_9_first_stage),
                style = MaterialTheme.typography.section,
                modifier = Modifier
                    .padding(vertical = 20.dp)
                    .navigationBarsPadding(bottom = false)
            )

            if (state.blockVersionVisible) {
                LabelValue(
                    R.string.block_version,
                    state.blockVersion.toFormattedString(LocalContext.current)
                )
            }
            if (state.blockStatusVisible) {
                LabelValue(
                    R.string.block_status,
                    state.blockStatus.toFormattedString(LocalContext.current)
                )
            }
            if (state.landingTypeVisible) {
                LabelValue(R.string.landing_type, stringResource(id = state.landingType))
            }
            if (state.landingShipVisible) {
                LabelValue(R.string.landing_ship, stringResource(id = state.landingShip))
            }
            if (state.landingZoneVisible) {
                LabelValue(R.string.landing_zone, stringResource(id = state.landingZone))
            }
        }
    }
}