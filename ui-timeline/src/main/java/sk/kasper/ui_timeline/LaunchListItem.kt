package sk.kasper.ui_timeline

import org.threeten.bp.LocalDateTime
import sk.kasper.domain.model.Launch
import sk.kasper.domain.model.Tag

data class LaunchListItem(
        val id: String,
        val launchName: String,
        val launchDateTime: LocalDateTime,
        val rocketId: Long?,
        val rocketName: String?,
        val accurateDate: Boolean,
        val accurateTime: Boolean,
        val tags: List<Tag>): TimelineListItem {

    companion object {

        fun fromLaunch(launch: Launch) = LaunchListItem(
                launch.id,
                launch.launchName,
                launch.launchDateTime,
                launch.rocketId,
                launch.rocketName,
                launch.accurateDate,
                launch.accurateTime,
                launch.tags)

    }

    override fun getType() = TimelineListItem.LAUNCH_TYPE

}