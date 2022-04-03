package sk.kasper.space.work

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoMap
import sk.kasper.space.notification.ShowLaunchNotificationWorker
import sk.kasper.space.sync.SyncWorker

@InstallIn(SingletonComponent::class)
@Module
interface WorkerBindingModule {

    @Binds
    @IntoMap
    @WorkerKey(SyncWorker::class)
    fun bindSyncWorkerFactory(factory: SyncWorker.Factory): ChildWorkerFactory

    @Binds
    @IntoMap
    @WorkerKey(ShowLaunchNotificationWorker::class)
    fun bindShowLaunchNotificationWorkerFactory(factory: ShowLaunchNotificationWorker.Factory): ChildWorkerFactory

}