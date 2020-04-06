package sk.kasper.domain.model

interface SyncLaunches {

    suspend fun doSync(force: Boolean = true): Boolean
    fun addSyncListener(syncListener: SyncListener)
    fun removeSyncListener(syncListener: SyncListener)

    interface SyncListener {
        fun onSync()
    }

}