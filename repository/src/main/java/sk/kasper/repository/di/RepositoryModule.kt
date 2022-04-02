package sk.kasper.repository.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import sk.kasper.repository.*
import sk.kasper.repository.impl.*

@InstallIn(SingletonComponent::class)
@Module
class RepositoryModule {

    @Provides
    internal fun providesRocketRepository(rocketRepositoryImpl: RocketRepositoryImpl): RocketRepository =
        rocketRepositoryImpl

    @Provides
    internal fun providesPhotoRepository(photoRepositoryImpl: PhotoRepositoryImpl): PhotoRepository =
        photoRepositoryImpl

    @Provides
    internal fun providesLaunchRepository(launchRepositoryImpl: LaunchRepositoryImpl): LaunchRepository =
        launchRepositoryImpl

    @Provides
    internal fun providesFalconInfoRepository(falconInfoRepositoryImpl: FalconInfoRepositoryImpl): FalconInfoRepository =
        falconInfoRepositoryImpl

    @Provides
    internal fun providesLaunchSiteRepository(launchSiteRepositoryImpl: LaunchSiteRepositoryImpl): LaunchSiteRepository =
        launchSiteRepositoryImpl

}