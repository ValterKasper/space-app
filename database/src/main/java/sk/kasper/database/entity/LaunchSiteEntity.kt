package sk.kasper.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import sk.kasper.entity.LaunchSite
import sk.kasper.entity.Position

@Entity(tableName = "launchSite")
data class LaunchSiteEntity(
        @PrimaryKey val id: Long,
        val launchSiteName: String,
        val launchPadName: String?,
        val launchSiteUrl: String?,
        val latitude: Double,
        val longitude: Double
) {

    fun toLaunchSite() = LaunchSite(Position(latitude, longitude), launchSiteName)
}