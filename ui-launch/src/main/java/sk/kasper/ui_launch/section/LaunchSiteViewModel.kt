package sk.kasper.ui_launch.section

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import sk.kasper.base.GoogleApiHelper
import sk.kasper.domain.model.ErrorResponse
import sk.kasper.domain.model.Response
import sk.kasper.domain.usecase.GetLaunchSite
import sk.kasper.entity.LaunchSite
import sk.kasper.ui_launch.R
import javax.inject.Inject

data class LaunchSizeState(
    val launchSite: LaunchSite? = null,
    val title: Int = R.string.section_launch_site,
    val visible: Boolean = false
)

@HiltViewModel
class LaunchSiteViewModel @Inject constructor(
    private val handle: SavedStateHandle,
    private val getLaunchSite: GetLaunchSite,
    private val googleApiHelper: GoogleApiHelper,
) : LoaderViewModel<LaunchSizeState, LaunchSite>(LaunchSizeState()) {

    init {
        loadAction()
    }

    override suspend fun load(): Response<LaunchSite> {
        return if (googleApiHelper.isGoogleApiAvailable()) {
            getLaunchSite(handle.get("launchId")!!)
        } else {
            ErrorResponse("google api not available")
        }
    }

    override fun mapLoadToState(load: LaunchSite, oldState: LaunchSizeState): LaunchSizeState {
        return oldState.copy(launchSite = load, visible = true)
    }

    override fun mapErrorToState(message: String?, oldState: LaunchSizeState): LaunchSizeState {
        return oldState.copy(visible = false)
    }

}