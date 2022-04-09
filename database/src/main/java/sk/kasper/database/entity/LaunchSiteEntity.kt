package sk.kasper.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "launchSite")
data class LaunchSiteEntity(
        @PrimaryKey val id: Long,
        val launchSiteName: String,
        val launchPadName: String?,
        val launchSiteUrl: String?,
        val latitude: Double,
        val longitude: Double
)