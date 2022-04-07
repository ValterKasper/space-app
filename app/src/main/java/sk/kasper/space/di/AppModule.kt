package sk.kasper.space.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import sk.kasper.base.init.AppInitializer
import sk.kasper.base.notification.EnqueueLaunchNotification
import sk.kasper.base.notification.NotificationsHelper
import sk.kasper.space.notification.EnqueueLaunchNotificationImpl
import sk.kasper.space.notification.LaunchNotificationsInitializer
import sk.kasper.space.notification.NotificationsHelperImpl
import sk.kasper.space.sync.SyncWorkManagerInitializer


@InstallIn(SingletonComponent::class)
@Module
class AppModule {

    @Provides
    fun providesNotificationsHelper(impl: NotificationsHelperImpl): NotificationsHelper = impl

    @Provides
    fun providesLaunchNotificationScheduler(launchNotificationScheduler: EnqueueLaunchNotificationImpl):
            EnqueueLaunchNotification = launchNotificationScheduler

    @Provides
    @IntoSet
    internal fun providesSyncWorkManagerInitializer(initializer: SyncWorkManagerInitializer): AppInitializer =
        initializer

    @Provides
    @IntoSet
    internal fun providesLaunchNotificationsInitializer(initializer: LaunchNotificationsInitializer): AppInitializer =
        initializer

}
