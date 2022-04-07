package sk.kasper.space.sync

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
        SyncWorker.startPeriodicWork(context, flags)
    }

}