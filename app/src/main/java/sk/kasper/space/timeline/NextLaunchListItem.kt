package sk.kasper.space.timeline

import org.threeten.bp.LocalDateTime
import sk.kasper.domain.model.Launch
import sk.kasper.domain.model.Tag

class NextLaunchListItem(
        val id: Long,
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