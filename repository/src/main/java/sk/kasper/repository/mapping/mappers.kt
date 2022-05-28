package sk.kasper.repository.mapping

import sk.kasper.base.utils.safeEnumValue
import sk.kasper.base.utils.toLocalDateTime
import sk.kasper.database.entity.*
import sk.kasper.entity.*
import sk.kasper.remote.entity.*


internal fun RemoteLaunchSite.toLaunchSiteEntity() =
    LaunchSiteEntity(id, siteName, padName, infoURL, latitude, longitude)

internal fun RemoteFalconCore.toFalconCoreEntity() = FalconCoreEntity(
    reused,
    block,
    flights,
    landingType.type,
    landingVehicle.type
)

internal fun RemoteLaunch.toLaunchEntity() = LaunchEntity(
    id,
    launchTs.toLocalDateTime(),
    mission_name ?: "",
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
    falconInfo?.cores?.firstOrNull()?.toFalconCoreEntity()
)

internal fun RemotePhoto.toPhotoEntity(): PhotoEntity {
    return PhotoEntity(id, thumbnailUrl, fullSizeUrl, sourceName, description)
}

internal fun RemoteRocket.toRocketEntity(): RocketEntity {
    return RocketEntity(id, rocketName, height, diameter, mass, payloadLeo, payloadGto, thrust, stages, manufacturerId)
}

fun FalconCoreEntity.toFalconCore() = FalconCore(
    reused!!,
    block!!,
    flights!!,
    safeEnumValue(landingType!!, FalconCore.LandingType.UNKNOWN),
    safeEnumValue(landingVehicle!!, FalconCore.LandingVehicle.UNKNOWN)
)

fun LaunchDetailEntity.toLaunch(): Launch =
    Launch(
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
        tags.map { it.toTag() })


fun LaunchSiteEntity.toLaunchSite() = LaunchSite(Position(latitude, longitude), launchSiteName)

fun PhotoEntity.toPhoto(): Photo {
    return Photo(thumbnailUrl, fullSizeUrl, sourceName, description)
}

fun RocketEntity.toRocket(): Rocket {
    return Rocket(id, rocketName, height, diameter, mass, payloadLeo, payloadGto, thrust, stages)
}

fun TagEntity.toTag(): Tag {
    return Tag(launchId, type)
}
