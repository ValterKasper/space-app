package sk.kasper.repository

interface SyncLaunchesRepository {

    suspend fun doSync(force: Boolean = true): Boolean

}