package sk.kasper.space.sync

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import dagger.hilt.android.AndroidEntryPoint
import sk.kasper.base.Flags
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class StartSyncJobReceiver : BroadcastReceiver() {

    @Inject
    lateinit var flags: Flags

    override fun onReceive(context: Context, intent: Intent?) {
        Timber.d("onReceive: intent = $intent")

        if (intent != null && intent.action == "android.intent.action.BOOT_COMPLETED") {
            SyncWorker.startPeriodicWork(context, flags)
        }
    }

}