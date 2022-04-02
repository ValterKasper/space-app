package sk.kasper.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "car")
data class Car(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    val name: String
)
