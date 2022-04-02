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
import sk.kasper.repository.SyncLaunchesRepository
import sk.kasper.space.mapper.RocketMapperImpl
import sk.kasper.space.mapper.TagMapperImpl
import sk.kasper.space.sync.SyncLaunchesRepositoryImpl
import sk.kasper.space.utils.GoogleApiHelperImpl
import sk.kasper.space.work.AppWorkerFactory
import sk.kasper.ui_common.rocket.RocketMapper
import sk.kasper.ui_common.tag.MapToDomainTag
import sk.kasper.ui_common.tag.MapToUiTag
import sk.kasper.ui_launch.usecase.GoogleApiHelper
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
    @Singleton
    fun providesSyncLaunches(syncLaunchesImpl: SyncLaunchesRepositoryImpl): SyncLaunchesRepository = syncLaunchesImpl

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
