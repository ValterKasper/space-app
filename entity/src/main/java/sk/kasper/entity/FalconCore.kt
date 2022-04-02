package sk.kasper.entity

data class FalconCore(
    val reused: Boolean,
    val block: Int,
    val flights: Int,
    val landingType: LandingType,
    val landingVehicle: LandingVehicle
) {

    enum class LandingType {
        ASDS,
        RTLS,
        OCEAN,
        UNKNOWN
    }

    enum class LandingVehicle {
        OCISLY,
        LZ_1,
        LZ_2,
        JRTI,
        UNKNOWN
    }

}