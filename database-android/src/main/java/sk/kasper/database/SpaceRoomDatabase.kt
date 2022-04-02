package sk.kasper.database

import androidx.room.Database
import androidx.room.RoomDatabase
import sk.kasper.database.entity.Car

@Database(
    entities = [
        Car::class
    ],
    version = 1
)
abstract class SpaceRoomDatabase : RoomDatabase(), SpaceDatabase