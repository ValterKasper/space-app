package sk.kasper.ui_launch

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import org.threeten.bp.LocalDateTime
import sk.kasper.domain.usecase.GetLaunch
import sk.kasper.ui_common.analytics.Analytics
import sk.kasper.ui_common.mapper.MapToUiTag
import sk.kasper.ui_common.tag.FilterTag
import sk.kasper.ui_common.utils.FormattedTimeType
import sk.kasper.ui_common.viewmodel.ReducerViewModel
import javax.inject.Inject

data class LaunchState(
    val missionName: String = "",
    val rocketName: String = "",
    val description: String = "",
    val launchDateTime: LocalDateTime = LocalDateTime.MIN,
    val formattedTimeType: FormattedTimeType = FormattedTimeType.FULL,
    val formattedTimeVisible: Boolean = true,
    val tags: List<FilterTag> = emptyList(),
    val mainPhoto: String? = null,
    val mainPhotoFallback: Int = R.drawable.ic_launch_fallback_main_photo,
    val dateConfirmed: Boolean = true,
    val videoUrl: String = "",
    val showVideoUrl: Boolean = false
)

sealed class LaunchSideEffect
data class ShowVideo(val url: String) : LaunchSideEffect()

@HiltViewModel
class LaunchViewModel @Inject constructor(
    private val getLaunch: GetLaunch,
    private val toUiTag: MapToUiTag,
    private val handle: SavedStateHandle,
) : ReducerViewModel<LaunchState, LaunchSideEffect>(LaunchState()) {

    init {
        init()
    }

    fun onVideoClick() = action {
        val url = snapshot().videoUrl
        emitSideEffect(ShowVideo(url))
    }

    private fun init() = action {
        val launchId: String = handle.get("launchId")!!
        val launch = getLaunch(launchId)
        val launchNameParts = launch.launchNameParts

        val dateConfirmed: Boolean
        val formattedTimeType: FormattedTimeType
        if (!launch.accurateDate) {
            dateConfirmed = false
            formattedTimeType = FormattedTimeType.FULL
        } else if (!launch.accurateTime) {
            dateConfirmed = true
            formattedTimeType = FormattedTimeType.DATE
        } else {
            //  accurate date and time
            dateConfirmed = true
            formattedTimeType = FormattedTimeType.FULL
        }

        // todo pories analytics
        Analytics.log(
            Analytics.Event.SELECT_CONTENT, hashMapOf(
                Analytics.Param.ITEM_ID to launchId,
                Analytics.Param.CONTENT_TYPE to (launch.rocketName ?: ""),
                Analytics.Param.ROCKET_ID to launch.rocketId.toString(),
                Analytics.Param.ITEM_NAME to launch.launchName
            )
        )
        reduce {
            copy(
                missionName = launchNameParts.missionName ?: "",
                rocketName = launch.rocketName ?: launchNameParts.rocketName ?: "",
                launchDateTime = launch.launchDateTime,
                description = launch.description ?: "",
                mainPhoto = launch.mainPhotoUrl,
                tags = launch.tags.map { toUiTag(it.type) },
                videoUrl = launch.videoUrl ?: "",
                showVideoUrl = launch.videoUrl != null,
                dateConfirmed = dateConfirmed,
                formattedTimeType = formattedTimeType
            )
        }
    }

}