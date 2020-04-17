package sk.kasper.space.sync

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import timber.log.Timber

class StartSyncJobReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        Timber.d("onReceive: intent = $intent")

        if (intent != null && intent.action == "android.intent.action.BOOT_COMPLETED") {
            SyncWorker.startPeriodicWork(context)
        }
    }

}