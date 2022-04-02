package sk.kasper.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
        tableName = "photo_launch",
        primaryKeys = [ "photoId", "launchId" ],
        foreignKeys = [
            ForeignKey(
                    entity = PhotoEntity::class,
                    parentColumns = ["id"],
                    childColumns = ["photoId"]
            ),
            ForeignKey(
                    entity = LaunchEntity::class,
                    parentColumns = ["id"],
                    childColumns = ["launchId"],
                    onDelete = ForeignKey.CASCADE
            )
        ],
        indices = [
            Index("photoId"),
            Index("launchId")
        ])
data class PhotoLaunchEntity(
        val photoId: Long,
        val launchId: String
)