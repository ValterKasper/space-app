package sk.kasper.space.di

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import androidx.work.Configuration
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import sk.kasper.domain.model.SyncLaunches
import sk.kasper.domain.repository.*
import sk.kasper.space.database.*
import sk.kasper.space.mapper.RocketMapperImpl
import sk.kasper.space.mapper.TagMapperImpl
import sk.kasper.space.repository.*
import sk.kasper.space.sync.SyncLaunchesImpl
import sk.kasper.space.utils.GoogleApiHelperImpl
import sk.kasper.space.work.AppWorkerFactory
import sk.kasper.ui_common.rocket.RocketMapper
import sk.kasper.ui_common.tag.MapToDomainTag
import sk.kasper.ui_common.tag.MapToUiTag
import sk.kasper.ui_launch.usecase.GoogleApiHelper
import javax.inject.Named
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
class AppModule {


    @Singleton
    @Provides
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(context)
    }

    @Provides
    fun providesLaunchDao(database: Database): LaunchDao {
        return database.launchDao()
    }

    @Provides
    fun providesLaunchSiteDao(database: Database): LaunchSiteDao {
        return database.launchSiteDao()
    }

    @Provides
    fun providesRocketDao(database: Database): RocketDao {
        return database.rocketDao()
    }

    @Provides
    fun providesPhotoDao(database: Database): PhotoDao {
        return database.photoDao()
    }

    @Provides
    fun providesRocketRepository(rocketRepositoryImpl: RocketRepositoryImpl): RocketRepository = rocketRepositoryImpl

    @Provides
    fun providesPhotoRepository(photoRepositoryImpl: PhotoRepositoryImpl): PhotoRepository = photoRepositoryImpl

    @Provides
    fun providesLaunchRepository(launchRepositoryImpl: LaunchRepositoryImpl): LaunchRepository = launchRepositoryImpl

    @Provides
    fun providesFalconInfoRepository(falconInfoRepositoryImpl: FalconInfoRepositoryImpl): FalconInfoRepository =
        falconInfoRepositoryImpl

    @Provides
    fun providesLaunchSiteRepository(launchSiteRepositoryImpl: LaunchSiteRepositoryImpl): LaunchSiteRepository =
        launchSiteRepositoryImpl

    @Provides
    @Singleton
    fun providesSyncLaunches(syncLaunchesImpl: SyncLaunchesImpl): SyncLaunches = syncLaunchesImpl

    @Provides
    @Singleton
    fun providesMapToUiTag(mapper: TagMapperImpl): MapToUiTag = mapper

    @Provides
    @Singleton
    fun providesMapToDomainTag(mapper: TagMapperImpl): MapToDomainTag = mapper

    @Provides
    @Singleton
    fun providesRocketMapper(mapper: RocketMapperImpl): RocketMapper = mapper

    @Provides
    @Named("Main")
    fun providesMainCoroutineDispatcher(): CoroutineDispatcher {
        return Dispatchers.Main
    }

    @Provides
    @Named("ViewModel")
    fun providesViewModelCoroutineDispatcher(): CoroutineDispatcher {
        return Dispatchers.Main
    }

    @Provides
    fun providesWorkManagerConfiguration(appWorkerFactory: AppWorkerFactory): Configuration {
        return Configuration.Builder()
            .setWorkerFactory(appWorkerFactory)
            .build()
    }

    @Provides
    @Singleton
    fun providesGoogleApiHelper(googleApiHelper: GoogleApiHelperImpl): GoogleApiHelper =
        googleApiHelper
}
