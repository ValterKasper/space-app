package sk.kasper.space.database.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import sk.kasper.domain.model.Photo


@Entity(tableName = "photo", indices = [Index("id")])
data class PhotoEntity(
        @PrimaryKey val id: Long,
        val thumbnailUrl: String,
        val fullSizeUrl: String,
        val sourceName: String?,
        val description: String?
) {

    fun toPhoto(): Photo {
        return Photo(thumbnailUrl, fullSizeUrl, sourceName, description)
    }

}