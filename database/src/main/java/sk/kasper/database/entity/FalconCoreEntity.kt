package sk.kasper.database.entity

import sk.kasper.entity.FalconCore
import sk.kasper.space.utils.safeEnumValue

data class FalconCoreEntity(val reused: Boolean?,
                            val block: Int?,
                            val flights: Int?,
                            val landingType: String?,
                            val landingVehicle: String?) {

    companion object {
        const val EMBEDDED_PREFIX = "falconCore_"
    }

    fun toFalconCore() = FalconCore(
        reused!!,
        block!!,
        flights!!,
        safeEnumValue(landingType!!, FalconCore.LandingType.UNKNOWN),
        safeEnumValue(landingVehicle!!, FalconCore.LandingVehicle.UNKNOWN)
    )

    fun isEmpty(): Boolean {
        return reused == null || block == null || flights == null || landingType == null || landingVehicle == null
    }

}