package sk.kasper.remote.entity

import sk.kasper.database.entity.FalconCoreEntity

data class RemoteFalconCore(
        val reused: Boolean,
        val block: Int,
        val flights: Int,
        val landingType: RemoteSpaceXLandingType,
        val landingVehicle: RemoteSpaceXLandingVehicle) {

    fun toFalconCoreEntity() = FalconCoreEntity(
            reused,
            block,
            flights,
            landingType.type,
            landingVehicle.type
    )
}