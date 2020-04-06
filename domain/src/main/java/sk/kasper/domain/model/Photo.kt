package sk.kasper.domain.model

data class Photo(val thumbnailUrl: String,
                 val fullSizeUrl: String = thumbnailUrl,
                 val sourceName: String? = null,
                 val description: String? = null)