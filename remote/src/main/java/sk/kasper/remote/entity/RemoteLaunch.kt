package sk.kasper.remote.entity

import sk.kasper.database.entity.LaunchEntity

data class RemoteLaunch(
        val id: String,
        val launchTs: Long,
        val name: String,
        val missionInfo: RemoteMissionInfo?,
        val mainPhotoUrl: String?,
        val rocketId: Long?,
        val orbit: RemoteOrbit?,
        val hashTag: String?,
        val payloadMass: Int?,
        val videoUrl: String?,
        val accurateDate: Boolean = false,
        val accurateTime: Boolean = false,
        val launchSiteId: Long?,
        val photos: List<Long>?,
        val tags: List<RemoteTag>,
        val falconInfo: RemoteFalconInfo?) {

    fun toLaunchEntity() = LaunchEntity(
            id,
            launchTs,
            name,
            missionInfo?.description,
            mainPhotoUrl,
            rocketId,
            orbit?.type,
            hashTag,
            payloadMass,
            videoUrl,
            accurateDate,
            accurateTime,
            launchSiteId,
            falconInfo?.cores?.firstOrNull()?.toFalconCoreEntity())

}