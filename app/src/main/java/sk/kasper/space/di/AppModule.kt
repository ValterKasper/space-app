package sk.kasper.space.di

import androidx.work.Configuration
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import sk.kasper.base.notification.EnqueueLaunchNotification
import sk.kasper.base.notification.NotificationsHelper
import sk.kasper.space.notification.EnqueueLaunchNotificationImpl
import sk.kasper.space.notification.NotificationsHelperImpl
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

    @Provides
    fun providesNotificationsHelper(impl: NotificationsHelperImpl): NotificationsHelper = impl

    @Provides
    fun providesLaunchNotificationScheduler(launchNotificationScheduler: EnqueueLaunchNotificationImpl):
            EnqueueLaunchNotification = launchNotificationScheduler
}
