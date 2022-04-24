package sk.kasper.database.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import sk.kasper.database.SpaceRoomDatabase

@InstallIn(SingletonComponent::class)
@Module
internal class DaoModule {

    @Provides
    internal fun providesLaunchDao(database: SpaceRoomDatabase) = database.launchDao()

    @Provides
    internal fun providesLaunchSiteDao(database: SpaceRoomDatabase) = database.launchSiteDao()

    @Provides
    internal fun providesRocketDao(database: SpaceRoomDatabase) = database.rocketDao()

    @Provides
    internal fun providesPhotoDao(database: SpaceRoomDatabase) = database.photoDao()

}