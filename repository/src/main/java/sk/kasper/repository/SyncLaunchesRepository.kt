package sk.kasper.repository

interface SyncLaunchesRepository {

    suspend fun doSync(force: Boolean = true): Boolean
    fun addSyncListener(syncListener: SyncListener)
    fun removeSyncListener(syncListener: SyncListener)

    interface SyncListener {
        fun onSync()
    }

}