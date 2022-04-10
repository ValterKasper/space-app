package sk.kasper.work.work

import android.content.Context
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.WorkManager
import dagger.hilt.android.qualifiers.ApplicationContext
import sk.kasper.base.Flags
import sk.kasper.base.init.AppInitializer
import sk.kasper.work.sync.SyncWorker
import javax.inject.Inject

internal class WorkManagerInitializer @Inject constructor(
    private val appWorkerFactory: HiltWorkerFactory,
    @ApplicationContext private val context: Context,
    private val flags: Flags,
) : AppInitializer {

    override fun init() {
        val configuration = Configuration.Builder()
            .setWorkerFactory(appWorkerFactory)
            .build()

        WorkManager.initialize(context, configuration)

        SyncWorker.startPeriodicWork(context, flags)
    }

}
