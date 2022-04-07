package sk.kasper.work.work

import android.content.Context
import androidx.work.Configuration
import androidx.work.WorkManager
import dagger.hilt.android.qualifiers.ApplicationContext
import sk.kasper.base.init.AppInitializer
import javax.inject.Inject

internal class WorkManagerInitializer @Inject constructor(
    private val appWorkerFactory: AppWorkerFactory,
    @ApplicationContext private val context: Context
) : AppInitializer {

    override fun init() {
        val configuration = Configuration.Builder()
            .setWorkerFactory(appWorkerFactory)
            .build()

        WorkManager.initialize(context, configuration)
    }

}
