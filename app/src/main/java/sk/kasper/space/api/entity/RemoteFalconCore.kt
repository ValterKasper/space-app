package sk.kasper.space.api.entity

import sk.kasper.space.database.entity.FalconCoreEntity

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