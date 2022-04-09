package sk.kasper.database.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(tableName = "photo", indices = [Index("id")])
data class PhotoEntity(
        @PrimaryKey val id: Long,
        val thumbnailUrl: String,
        val fullSizeUrl: String,
        val sourceName: String?,
        val description: String?
)