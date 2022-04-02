package sk.kasper.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "manufacturer")
data class ManufacturerEntity(
        @PrimaryKey val id: Int,
        val manufacturerName: String,
        val countryCode: String,
        val infoUrl: String)