package sk.kasper.work.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import sk.kasper.base.init.AppInitializer
import sk.kasper.base.notification.EnqueueLaunchNotification
import sk.kasper.work.notification.EnqueueLaunchNotificationImpl
import sk.kasper.work.notification.LaunchNotificationsInitializer
import sk.kasper.work.work.WorkManagerInitializer

@InstallIn(SingletonComponent::class)
@Module
internal interface WorkerBindingModule {

    @Binds
    fun providesLaunchNotificationScheduler(launchNotificationScheduler: EnqueueLaunchNotificationImpl):
            EnqueueLaunchNotification

    @Binds
    @IntoSet
    fun providesLaunchNotificationsInitializer(initializer: LaunchNotificationsInitializer): AppInitializer

    @Binds
    @IntoSet
    fun providesWorkManagerInitializer(initializer: WorkManagerInitializer): AppInitializer

}