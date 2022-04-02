package sk.kasper.domain.utils

import org.threeten.bp.LocalDateTime
import sk.kasper.entity.Launch
import sk.kasper.entity.Tag

fun createLaunch(
        id: String = "",
        launchName: String = "",
        description: String? = null,
        launchDateTime: LocalDateTime = LocalDateTime.MIN,
        mainPhotoUrl: String? = null,
        hashTag: String? = null,
        payloadMass: Int? = null,
        rocketId: Long? = null,
        rocketName: String? = null,
        videoUrl: String? = null,
        accurateDate: Boolean = true,
        accurateTime: Boolean = true,
        manufacturerName: String? = null,
        tags: List<Tag> = emptyList()
) = Launch(
        id,
        launchName,
        description,
        launchDateTime,
        mainPhotoUrl,
        hashTag,
        payloadMass,
        rocketId,
        rocketName,
        videoUrl,
        accurateDate,
        accurateTime,
        manufacturerName,
        tags)
