package sk.kasper.space.database.entity

import sk.kasper.domain.model.FalconCore
import sk.kasper.space.utils.safeEnumValueOf

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
            safeEnumValueOf(landingType!!, FalconCore.LandingType.UNKNOWN),
            safeEnumValueOf(landingVehicle!!, FalconCore.LandingVehicle.UNKNOWN)
    )

    fun isEmpty(): Boolean {
        return reused == null || block == null || flights == null || landingType == null || landingVehicle == null
    }

}