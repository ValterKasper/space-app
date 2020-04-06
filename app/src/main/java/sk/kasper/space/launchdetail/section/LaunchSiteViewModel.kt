package sk.kasper.space.launchdetail.section

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import sk.kasper.domain.model.LaunchSite
import sk.kasper.domain.model.SuccessResponse
import sk.kasper.domain.usecase.launchdetail.GetLaunchSite
import sk.kasper.space.BR
import sk.kasper.space.R
import javax.inject.Inject

class LaunchSiteViewModel @Inject constructor(
        private val getLaunchSite: GetLaunchSite): SectionViewModel() {

    val launchSite: MutableLiveData<LaunchSite> = MutableLiveData()

    init {
        title = R.string.section_launch_site
    }

    // todo assited
    var launchId: Long = 0L
        set(value) {
            if (field == 0L) {
                field = value

                viewModelScope.launch {
                    val launchSiteResponse = getLaunchSite.getLaunchSite(launchId)
                    when (launchSiteResponse) {
                        is SuccessResponse -> {
                            launchSite.value = launchSiteResponse.data
                        }
                        else -> {
                            visible = false
                            notifyPropertyChanged(BR.visible)
                        }
                    }
                }
            }
        }

    var googleApiAvailable = true
        set(value) {
            field = value
            visible = value
            notifyPropertyChanged(BR.visible)
        }


}