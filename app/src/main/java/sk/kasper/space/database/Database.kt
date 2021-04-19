package sk.kasper.space.database

import androidx.room.Database
import androidx.room.RoomDatabase
import sk.kasper.space.database.entity.*

@Database(
        entities =
            [LaunchEntity::class,
            RocketEntity::class,
            LaunchSiteEntity::class,
            ManufacturerEntity::class,
            TagEntity::class,
            PhotoEntity::class,
            PhotoLaunchEntity::class],
        version = 6,
        exportSchema = false)
abstract class Database : RoomDatabase() {
    abstract fun launchDao(): LaunchDao
    abstract fun launchSiteDao(): LaunchSiteDao
    abstract fun rocketDao(): RocketDao
    abstract fun tagDao(): TagDao
    abstract fun photoDao(): PhotoDao
}