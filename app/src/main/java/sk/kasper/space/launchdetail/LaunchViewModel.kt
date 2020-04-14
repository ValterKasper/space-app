package sk.kasper.space.launchdetail

import androidx.annotation.DrawableRes
import androidx.databinding.Bindable
import androidx.lifecycle.viewModelScope
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDateTime
import sk.kasper.domain.model.Tag
import sk.kasper.domain.usecase.launchdetail.GetLaunch
import sk.kasper.space.R
import sk.kasper.space.analytics.Analytics
import sk.kasper.space.utils.FormattedTimeType
import sk.kasper.space.utils.ObservableViewModel
import sk.kasper.space.utils.SingleLiveEvent
import timber.log.Timber

class LaunchViewModel @AssistedInject constructor(
        private val getLaunch: GetLaunch,
        @Assisted private val launchId: Long): ObservableViewModel() {

    @get:Bindable
    var missionName: String? = null

    @get:Bindable
    var rocketName: String? = null

    @get:Bindable
    var rocketNameVisible: Boolean = true

    @get:Bindable
    var description: String? = null

    @get:Bindable
    var descriptionVisible:  Boolean = true

    @get:Bindable
    var launchDateTime: LocalDateTime = LocalDateTime.MIN

    @get:Bindable
    var formattedTimeType: FormattedTimeType = FormattedTimeType.FULL

    @get:Bindable
    var formattedTimeVisible:  Boolean = true

    @get:Bindable
    var tags: MutableList<Tag> = mutableListOf()

    @get:Bindable
    var mainPhoto: String? = null

    @get:Bindable
    @DrawableRes
    var mainPhotoFallback: Int = R.drawable.ic_launch_fallback_main_photo

    @get:Bindable
    var tagsVisible: Boolean = true

    @get:Bindable
    var videoUrlVisible: Boolean = true

    @get:Bindable
    var dateConfirmed: Boolean = true

    val showVideoUrl: SingleLiveEvent<String> = SingleLiveEvent()

    private var videoUrl: String? = null

    init {
        loadLaunchDetail()
    }

    fun onVideoUrlClick() {
        videoUrl?.let {
            Analytics.log(Analytics.Event.WATCH_LIVE, mapOf(Analytics.Param.ITEM_ID to launchId.toString()))
            showVideoUrl.value = it
        }
    }

    private fun loadLaunchDetail() {
        viewModelScope.launch {
            try {
                val launch = getLaunch.getLaunch(launchId)
                val launchNameParts = launch.launchNameParts
                missionName = launchNameParts.missionName
                rocketName = launch.rocketName ?: launchNameParts.rocketName
                rocketNameVisible = rocketName.isNullOrBlank()
                launchDateTime = launch.launchDateTime
                description = launch.description
                descriptionVisible = !launch.description.isNullOrBlank()
                mainPhoto = launch.mainPhotoUrl
                tags.clear()
                tags.addAll(launch.tags)

                if (!launch.accurateDate) {
                    dateConfirmed = false
                } else if (!launch.accurateTime) {
                    formattedTimeType = FormattedTimeType.DATE
                }

                videoUrl = launch.videoUrl
                videoUrlVisible = videoUrl != null

                tagsVisible = tags.isNotEmpty()

                notifyChange()

                Analytics.log(Analytics.Event.SELECT_CONTENT, hashMapOf(
                        Analytics.Param.ITEM_ID to launchId.toString(),
                        Analytics.Param.CONTENT_TYPE to (rocketName ?: ""),
                        Analytics.Param.ROCKET_ID to launch.rocketId.toString(),
                        Analytics.Param.ITEM_NAME to launch.launchName
                ))
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(launchId: Long): LaunchViewModel
    }

}