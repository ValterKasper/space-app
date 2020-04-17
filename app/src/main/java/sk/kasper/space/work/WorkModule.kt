package sk.kasper.space.work

import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import sk.kasper.space.sync.SyncWorker

@Module(includes = [])
interface WorkerBindingModule {

    @Binds
    @IntoMap
    @WorkerKey(SyncWorker::class)
    fun bindSyncWorkerFactory(factory: SyncWorker.Factory): ChildWorkerFactory

}