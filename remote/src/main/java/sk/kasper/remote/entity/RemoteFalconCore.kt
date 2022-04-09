package sk.kasper.remote.entity

data class RemoteFalconCore(
    val reused: Boolean,
    val block: Int,
    val flights: Int,
    val landingType: RemoteSpaceXLandingType,
    val landingVehicle: RemoteSpaceXLandingVehicle
)