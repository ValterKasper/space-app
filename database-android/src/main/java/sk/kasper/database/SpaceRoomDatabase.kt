package sk.kasper.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import sk.kasper.database.converters.Converters
import sk.kasper.database.entity.*

@Database(
    entities =
    [
        LaunchEntity::class,
        RocketEntity::class,
        LaunchSiteEntity::class,
        ManufacturerEntity::class,
        TagEntity::class,
        PhotoEntity::class,
        PhotoLaunchEntity::class
    ],
    version = 6,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class SpaceRoomDatabase : RoomDatabase(), SpaceDatabase