package sk.kasper.ui_timeline

import org.threeten.bp.LocalDateTime
import sk.kasper.entity.Launch
import sk.kasper.entity.Tag

class NextLaunchListItem(
        val id: String,
        val launchName: String,
        val launchDateTime: LocalDateTime,
        val rocketId: Long?,
        val rocketName: String?,
        val tags: List<Tag>): TimelineListItem {

    companion object {

        fun fromLaunch(launch: Launch) = NextLaunchListItem(
                launch.id,
                launch.launchName,
                launch.launchDateTime,
                launch.rocketId,
                launch.rocketName,
                launch.tags)

    }

    override fun getType(): Int = TimelineListItem.NEXT_LAUNCH_TYPE

}