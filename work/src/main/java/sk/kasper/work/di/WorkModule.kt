package sk.kasper.work.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoMap
import dagger.multibindings.IntoSet
import sk.kasper.base.init.AppInitializer
import sk.kasper.base.notification.EnqueueLaunchNotification
import sk.kasper.work.notification.EnqueueLaunchNotificationImpl
import sk.kasper.work.notification.LaunchNotificationsInitializer
import sk.kasper.work.notification.ShowLaunchNotificationWorker
import sk.kasper.work.sync.SyncWorkManagerInitializer
import sk.kasper.work.sync.SyncWorker
import sk.kasper.work.work.ChildWorkerFactory
import sk.kasper.work.work.WorkManagerInitializer
import sk.kasper.work.work.WorkerKey

@InstallIn(SingletonComponent::class)
@Module
internal interface WorkerBindingModule {

    @Binds
    @IntoMap
    @WorkerKey(SyncWorker::class)
    fun bindSyncWorkerFactory(factory: SyncWorker.Factory): ChildWorkerFactory

    @Binds
    @IntoMap
    @WorkerKey(ShowLaunchNotificationWorker::class)
    fun bindShowLaunchNotificationWorkerFactory(factory: ShowLaunchNotificationWorker.Factory): ChildWorkerFactory

    @Binds
    fun providesLaunchNotificationScheduler(launchNotificationScheduler: EnqueueLaunchNotificationImpl):
            EnqueueLaunchNotification

    @Binds
    @IntoSet
    fun providesLaunchNotificationsInitializer(initializer: LaunchNotificationsInitializer): AppInitializer

    @Binds
    @IntoSet
    fun providesWorkManagerInitializer(initializer: WorkManagerInitializer): AppInitializer

    @Binds
    @IntoSet
    fun providesSyncWorkManagerInitializer(initializer: SyncWorkManagerInitializer): AppInitializer

}