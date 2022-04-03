package sk.kasper.space.sync

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import sk.kasper.base.Flags
import timber.log.Timber

// TODO D: inject here flags
class StartSyncJobReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        Timber.d("onReceive: intent = $intent")

        if (intent != null && intent.action == "android.intent.action.BOOT_COMPLETED") {
            SyncWorker.startPeriodicWork(context, object : Flags {
                override val isDebug: Boolean
                    get() = TODO("Not yet implemented")
                override val apiKey: String
                    get() = TODO("Not yet implemented")
                override val synIntervalHours: Long
                    get() = 4
            })
        }
    }

}