package sk.kasper.space.di

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import androidx.work.Configuration
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import sk.kasper.domain.model.SyncLaunches
import sk.kasper.domain.repository.*
import sk.kasper.space.database.*
import sk.kasper.space.repository.*
import sk.kasper.space.sync.SyncLaunchesImpl
import sk.kasper.space.work.SampleWorkerFactory
import javax.inject.Named
import javax.inject.Singleton


@Module
class AppModule(val context: Context) {


    @Singleton
    @Provides
    fun provideSharedPreferences(): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(context)
    }

    @Provides
    fun providesContext(): Context {
        return context
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
    fun providesFalconInfoRepository(falconInfoRepositoryImpl: FalconInfoRepositoryImpl): FalconInfoRepository = falconInfoRepositoryImpl

    @Provides
    fun providesLaunchSiteRepository(launchSiteRepositoryImpl: LaunchSiteRepositoryImpl): LaunchSiteRepository = launchSiteRepositoryImpl

    @Provides
    @Singleton
    fun providesSyncLaunches(syncLaunchesImpl: SyncLaunchesImpl): SyncLaunches = syncLaunchesImpl

    @Provides
    @Named("Main")
    fun providesMainCoroutineDispatcher(): CoroutineDispatcher {
        return Dispatchers.Main
    }

    @Provides
    fun providesWorkManagerConfiguration(sampleWorkerFactory: SampleWorkerFactory): Configuration {
        return Configuration.Builder()
                .setWorkerFactory(sampleWorkerFactory)
                .build()
    }

}
