package sk.kasper.remote.entity

import sk.kasper.database.entity.RocketEntity

data class RemoteRocket(val id: Long,
                        val rocketName: String,
                        val height: Float, // m
                        val diameter: Float, // m
                        val mass: Float, // kg
                        val payloadLeo: Float, // kg
                        val payloadGto: Float, // kg
                        val thrust: Float, // kN
                        val stages: Int,
                        val manufacturerId: Int?) {

    fun toRocketEntity(): RocketEntity {
        return RocketEntity(id, rocketName, height, diameter, mass, payloadLeo, payloadGto, thrust, stages, manufacturerId)
    }

}