package sk.kasper.database.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import sk.kasper.database.SpaceDatabase
import sk.kasper.database.SpaceRoomDatabase
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
internal class DatabaseModule {

    @Singleton
    @Provides
    internal fun providesDatabase(@ApplicationContext context: Context): SpaceDatabase {
        return Room.databaseBuilder(context, SpaceRoomDatabase::class.java, "local-database")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    internal fun providesLaunchDao(database: SpaceDatabase) = database.launchDao()

    @Provides
    internal fun providesLaunchSiteDao(database: SpaceDatabase) = database.launchSiteDao()

    @Provides
    internal fun providesRocketDao(database: SpaceDatabase) = database.rocketDao()

    @Provides
    internal fun providesPhotoDao(database: SpaceDatabase) = database.photoDao()

}