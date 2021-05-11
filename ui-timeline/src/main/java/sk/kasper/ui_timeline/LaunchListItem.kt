package sk.kasper.ui_timeline

import org.threeten.bp.LocalDateTime
import sk.kasper.domain.model.Launch
import sk.kasper.ui_common.tag.TagMapper
import sk.kasper.ui_common.tag.UiTag

data class LaunchListItem(
    val id: String,
    val launchName: String,
    val launchDateTime: LocalDateTime,
    val rocketId: Long?,
    val rocketName: String?,
    val accurateDate: Boolean,
    val accurateTime: Boolean,
    val tags: List<UiTag>
) : TimelineListItem {

    companion object {

        fun fromLaunch(launch: Launch, tagMapper: TagMapper) = LaunchListItem(
            launch.id,
            launch.launchName,
            launch.launchDateTime,
            launch.rocketId,
            launch.rocketName,
            launch.accurateDate,
            launch.accurateTime,
            launch.tags.map { tagMapper.toUiTag(it.type) })

    }

    override fun getType() = TimelineListItem.LAUNCH_TYPE

}