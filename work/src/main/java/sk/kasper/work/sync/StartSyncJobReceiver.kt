package sk.kasper.work.sync

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import dagger.hilt.android.AndroidEntryPoint
import sk.kasper.base.Flags
import sk.kasper.base.logger.Logger
import javax.inject.Inject

@AndroidEntryPoint
class StartSyncJobReceiver : BroadcastReceiver() {

    @Inject
    lateinit var flags: Flags

    override fun onReceive(context: Context, intent: Intent?) {
        Logger.d("onReceive: intent = $intent")

        if (intent != null && intent.action == "android.intent.action.BOOT_COMPLETED") {
            SyncWorker.startPeriodicWork(context, flags)
        }
    }

}