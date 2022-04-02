package sk.kasper.database.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import sk.kasper.database.SpaceRoomDatabase
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun providesDatabase(@ApplicationContext context: Context): SpaceRoomDatabase {
        return Room.databaseBuilder(context, SpaceRoomDatabase::class.java, "local-database")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun providesLaunchDao(database: SpaceRoomDatabase) = database.launchDao()

    @Provides
    fun providesLaunchSiteDao(database: SpaceRoomDatabase) = database.launchSiteDao()

    @Provides
    fun providesRocketDao(database: SpaceRoomDatabase) = database.rocketDao()

    @Provides
    fun providesPhotoDao(database: SpaceRoomDatabase) = database.photoDao()

}