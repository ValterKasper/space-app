package sk.kasper.space.di

import androidx.work.Configuration
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import sk.kasper.space.work.AppWorkerFactory


@InstallIn(SingletonComponent::class)
@Module
class AppModule {

    @Provides
    fun providesWorkManagerConfiguration(appWorkerFactory: AppWorkerFactory): Configuration {
        return Configuration.Builder()
            .setWorkerFactory(appWorkerFactory)
            .build()
    }
}
