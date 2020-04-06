package sk.kasper.space.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import sk.kasper.domain.model.LaunchSite
import sk.kasper.domain.model.Position

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