package sk.kasper.work.sync

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import sk.kasper.base.Flags
import sk.kasper.base.init.AppInitializer
import javax.inject.Inject

internal class SyncWorkManagerInitializer @Inject constructor(
    private val flags: Flags,
    @ApplicationContext private val context: Context
) : AppInitializer {

    override fun init() {
        // TODO D: handle order: should be called after work manager is initialized
//        SyncWorker.startPeriodicWork(context, flags)
    }

}