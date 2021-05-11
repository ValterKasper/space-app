package sk.kasper.ui_timeline

import androidx.annotation.DrawableRes
import org.threeten.bp.LocalDateTime
import sk.kasper.domain.model.Launch
import sk.kasper.ui_common.rocket.RocketMapper
import sk.kasper.ui_common.tag.TagMapper
import sk.kasper.ui_common.tag.UiTag

data class LaunchListItem(
    val id: String,
    val launchName: String,
    val launchDateTime: LocalDateTime,
    @DrawableRes val rocketResId: Int,
    val rocketName: String?,
    val accurateDate: Boolean,
    val accurateTime: Boolean,
    val tags: List<UiTag>
) : TimelineListItem {

    companion object {

        fun fromLaunch(launch: Launch, tagMapper: TagMapper, rocketMapper: RocketMapper) =
            LaunchListItem(
                launch.id,
                launch.launchName,
                launch.launchDateTime,
                rocketMapper.toDrawableRes(launch.rocketId),
                launch.rocketName,
                launch.accurateDate,
                launch.accurateTime,
                launch.tags.map { tagMapper.toUiTag(it.type) })
    }

    override fun getType() = TimelineListItem.LAUNCH_TYPE

}