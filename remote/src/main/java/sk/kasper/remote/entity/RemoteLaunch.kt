package sk.kasper.remote.entity

data class RemoteLaunch(
    val id: String,
    val launchTs: Long,
    val mission_name: String?,
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
    val falconInfo: RemoteFalconInfo?
)