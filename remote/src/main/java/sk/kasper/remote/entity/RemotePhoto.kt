package sk.kasper.remote.entity

import sk.kasper.database.entity.PhotoEntity

data class RemotePhoto(val id: Long,
                       val thumbnailUrl: String,
                       val fullSizeUrl: String,
                       val sourceName: String? = null,
                       val description: String? = null) {

    fun toPhotoEntity(): PhotoEntity {
        return PhotoEntity(id, thumbnailUrl, fullSizeUrl, sourceName, description)
    }

}