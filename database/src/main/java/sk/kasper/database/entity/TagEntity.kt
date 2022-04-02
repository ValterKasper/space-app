package sk.kasper.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.Index
import androidx.room.PrimaryKey
import sk.kasper.entity.Tag

@Entity(foreignKeys = arrayOf(
        ForeignKey(
                entity = LaunchEntity::class,
                parentColumns = arrayOf("id"),
                childColumns = arrayOf("launchId"),
                onDelete = CASCADE)),
        indices = arrayOf(Index("launchId", "type", unique = true)),
        tableName = "tag")
data class TagEntity(
        @PrimaryKey(autoGenerate = true) val id: Long? = null,
        val launchId: String,
        val type: Long) {
    
    fun toTag(): Tag {
        return Tag(launchId, type)
    }
}