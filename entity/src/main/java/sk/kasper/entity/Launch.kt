package sk.kasper.entity

import org.threeten.bp.LocalDateTime

data class Launch(
        val id: String,
        val launchName: String,
        val description: String?,
        val launchDateTime: LocalDateTime,
        val mainPhotoUrl: String?,
        val hashTag: String?,
        val payloadMass: Int?,
        val rocketId: Long?,
        val rocketName: String?,
        val videoUrl: String?,
        val accurateDate: Boolean,
        val accurateTime: Boolean,
        val manufacturerName: String?,
        val tags: List<Tag>) {

    data class LaunchNameParts(val rocketName: String?, val missionName: String?)

    val launchNameParts: LaunchNameParts
        get() {
            val parts = launchName.split("|").map { it.trim() }
            return LaunchNameParts(
                    parts.elementAtOrNull(0),
                    parts.elementAtOrNull(1))
        }

}