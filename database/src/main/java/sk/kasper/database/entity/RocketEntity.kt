package sk.kasper.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import sk.kasper.entity.Rocket

@Entity(
        foreignKeys = [ForeignKey(
                entity = ManufacturerEntity::class,
                parentColumns = arrayOf("id"),
                childColumns = arrayOf("manufacturerId")
        )],
        indices = [Index("manufacturerId")],
        tableName = "rocket"
)
data class RocketEntity(
        @PrimaryKey val id: Long,
        val rocketName: String,
        val height: Float,
        val diameter: Float,
        val mass: Float,
        val payloadLeo: Float,
        val payloadGto: Float,
        val thrust: Float,
        val stages: Int,
        val manufacturerId: Int?) {

    fun toRocket(): Rocket {
        return Rocket(id, rocketName, height, diameter, mass, payloadLeo, payloadGto, thrust, stages)
    }
}