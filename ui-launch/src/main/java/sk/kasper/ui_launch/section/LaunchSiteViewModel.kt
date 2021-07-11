package sk.kasper.ui_launch.section

import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import sk.kasper.domain.model.ErrorResponse
import sk.kasper.domain.model.LaunchSite
import sk.kasper.domain.model.Response
import sk.kasper.domain.usecase.launchdetail.GetLaunchSite
import sk.kasper.ui_launch.R

data class LaunchSizeState(
    val launchSite: LaunchSite? = null,
    val title: Int = R.string.section_launch_site,
    val visible: Boolean = false
)

class LaunchSiteViewModel @AssistedInject constructor(
    @Assisted private val launchId: String,
    @Assisted private val googleApiAvailable: Boolean, // todo malo by prist z usecasu; mozno by nemalo byt z LoaderViewModel
    private val getLaunchSite: GetLaunchSite
) : LoaderViewModel<LaunchSizeState, LaunchSite>(LaunchSizeState()) {

    init {
        loadAction()
    }

    override suspend fun load(): Response<LaunchSite> {
        return if (googleApiAvailable) {
            getLaunchSite.getLaunchSite(launchId)
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

    @AssistedFactory
    interface Factory {
        fun create(
            launchId: String,
            googleApiAvailable: Boolean
        ): LaunchSiteViewModel
    }
}