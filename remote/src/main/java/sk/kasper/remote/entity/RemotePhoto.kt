package sk.kasper.remote.entity

data class RemotePhoto(
    val id: Long,
    val thumbnailUrl: String,
    val fullSizeUrl: String,
    val sourceName: String? = null,
    val description: String? = null
)