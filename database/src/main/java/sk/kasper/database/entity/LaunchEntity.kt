package sk.kasper.database.entity

import androidx.room.*

@Entity(foreignKeys = arrayOf(
            ForeignKey(
                entity = RocketEntity::class,
                parentColumns = arrayOf("id"),
                childColumns = arrayOf("rocketId")),
            ForeignKey(
                entity = LaunchSiteEntity::class,
                parentColumns = arrayOf("id"),
                childColumns = arrayOf("launchSiteId"))),
        indices = arrayOf(
                Index("rocketId"),
                Index("launchSiteId")),
        tableName = "launch")
data class LaunchEntity(@PrimaryKey val id: String,
                        val launchTs: Long,
                        val launchName: String,
                        val description: String?,
                        val mainPhotoUrl: String?,
                        val rocketId: Long?,
                        val orbit: String?,
                        val hashTag: String?,
                        val payloadMass: Int?,
                        val videoUrl: String?,
                        val accurateDate: Boolean,
                        val accurateTime: Boolean,
                        val launchSiteId: Long?,
                        @Embedded(prefix = FalconCoreEntity.EMBEDDED_PREFIX) val falconCore: FalconCoreEntity?)