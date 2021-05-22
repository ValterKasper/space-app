package sk.kasper.ui_launch

import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.threeten.bp.LocalDateTime
import sk.kasper.domain.model.Launch
import sk.kasper.domain.usecase.launchdetail.GetLaunch
import sk.kasper.ui_common.analytics.Analytics
import sk.kasper.ui_common.tag.TagMapper
import sk.kasper.ui_common.tag.UiTag
import sk.kasper.ui_common.utils.FormattedTimeType
import sk.kasper.ui_common.viewmodel.ReducerViewModel
import timber.log.Timber

data class LaunchState(
    val missionName: String = "",
    val rocketName: String = "",
    val description: String = "",
    val launchDateTime: LocalDateTime = LocalDateTime.MIN,
    val formattedTimeType: FormattedTimeType = FormattedTimeType.FULL,
    val formattedTimeVisible: Boolean = true,
    val tags: List<UiTag> = emptyList(),
    val mainPhoto: String? = null,
    val mainPhotoFallback: Int = R.drawable.ic_launch_fallback_main_photo,
    val dateConfirmed: Boolean = true,
    val videoUrl: String = "",
    val showVideoUrl: Boolean = false
)

sealed class LaunchAction
object Init : LaunchAction()
object VideoClick : LaunchAction()
data class ShowLaunch(val launch: Launch) : LaunchAction()


sealed class LaunchSideEffect
data class ShowVideo(val url: String) : LaunchSideEffect()

class LaunchViewModel @AssistedInject constructor(
    private val getLaunch: GetLaunch,
    private val tagMapper: TagMapper,
    @Assisted private val launchId: String
) : ReducerViewModel<LaunchState, LaunchAction, LaunchSideEffect>(LaunchState()) {

    init {
        submitAction(Init)
    }

    override fun mapActionToActionFlow(action: LaunchAction): Flow<LaunchAction> {
        return if (action === Init) {
            flow {
                try {
                    emit(ShowLaunch(getLaunch.getLaunch(launchId = launchId)))
                } catch (e: Exception) {
                    Timber.e(e)
                }
            }
        } else {
            super.mapActionToActionFlow(action)
        }
    }

    override fun ScanScope.scan(action: LaunchAction, oldState: LaunchState): LaunchState {
        return when (action) {
            is ShowLaunch -> {
                val launch = action.launch
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

                oldState.copy(
                    missionName = launchNameParts.missionName ?: "",
                    rocketName = launch.rocketName ?: launchNameParts.rocketName ?: "",
                    launchDateTime = launch.launchDateTime,
                    description = launch.description ?: "",
                    mainPhoto = launch.mainPhotoUrl,
                    tags = launch.tags.map { tagMapper.toUiTag(it.type) },
                    videoUrl = launch.videoUrl ?: "",
                    showVideoUrl = launch.videoUrl != null,
                    dateConfirmed = dateConfirmed,
                    formattedTimeType = formattedTimeType
                )
            }
            VideoClick -> {
                Analytics.log(
                    Analytics.Event.WATCH_LIVE,
                    mapOf(Analytics.Param.ITEM_ID to launchId)
                ) // todo pories analytics
                emitSideEffect(ShowVideo(oldState.videoUrl))
                oldState
            }
            else -> oldState
        }
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(launchId: String): LaunchViewModel
    }

}