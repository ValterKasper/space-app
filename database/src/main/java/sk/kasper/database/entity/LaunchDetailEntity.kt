package sk.kasper.database.entity

import androidx.room.ColumnInfo
import androidx.room.Relation

data class LaunchDetailEntity(
        var id: String = "",
        var launchTs: Long = 0,
        var launchName: String = "",
        var description: String? = null,
        var mainPhotoUrl: String? = null,
        var hashTag: String? = null,
        var payloadMass: Int? = null,
        var rocketId: Long? = null,
        var videoUrl: String? = null,
        var accurateDate: Boolean = true,
        var accurateTime: Boolean = true,

        var rocketName: String? = null,

        @ColumnInfo(name = "manufacturerName")
        var manufacturerName: String? = null,
        @Relation(parentColumn = "id", entityColumn = "launchId")
        var tags: MutableList<TagEntity> = mutableListOf()
)