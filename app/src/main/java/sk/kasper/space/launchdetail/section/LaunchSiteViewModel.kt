package sk.kasper.space.launchdetail.section

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import kotlinx.coroutines.launch
import sk.kasper.domain.model.LaunchSite
import sk.kasper.domain.model.SuccessResponse
import sk.kasper.domain.usecase.launchdetail.GetLaunchSite
import sk.kasper.space.BR
import sk.kasper.space.R

class LaunchSiteViewModel @AssistedInject constructor(
        @Assisted launchId: String,
        @Assisted googleApiAvailable: Boolean,
        private val getLaunchSite: GetLaunchSite): SectionViewModel() {

    val launchSite: MutableLiveData<LaunchSite> = MutableLiveData()

    init {
        title = R.string.section_launch_site
        visible = googleApiAvailable

        viewModelScope.launch {
            when (val launchSiteResponse = getLaunchSite.getLaunchSite(launchId)) {
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

    @AssistedInject.Factory
    interface Factory {
        fun create(
                launchId: String,
                googleApiAvailable: Boolean): LaunchSiteViewModel
    }
}