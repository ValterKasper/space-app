package sk.kasper.space.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import sk.kasper.space.database.entity.LaunchSiteEntity

@Dao
abstract class LaunchSiteDao {

    // todo to raw string
    @Query("" +
            "SELECT " +
            "   launchSite.id, " +
            "   launchSite.launchSiteName, " +
            "   launchSite.launchPadName, " +
            "   launchSite.launchSiteUrl, " +
            "   launchSite.latitude, " +
            "   launchSite.longitude " +
            "FROM " +
            "   launch " +
            "LEFT JOIN launchSite " +
            "   ON launch.launchSiteId = launchSite.id " +
            "WHERE " +
            "   launch.id = :launchId")
    abstract fun getLaunchSiteByLaunchId(launchId: Long): LaunchSiteEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertAll(vararg launchSiteEntity: LaunchSiteEntity): List<Long>

}