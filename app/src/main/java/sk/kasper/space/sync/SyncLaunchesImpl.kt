package sk.kasper.space.sync

import android.content.SharedPreferences
import androidx.annotation.WorkerThread
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import sk.kasper.database.SpaceRoomDatabase
import sk.kasper.database.entity.LaunchAndTagsEntity
import sk.kasper.database.entity.PhotoLaunchEntity
import sk.kasper.database.entity.TagEntity
import sk.kasper.domain.model.SyncLaunches
import sk.kasper.space.api.RemoteApi
import sk.kasper.space.api.entity.RESPONSE_CODE_BAD_API_KEY
import timber.log.Timber
import javax.inject.Inject

// TODO D: change to repository and move to repository module
class SyncLaunchesImpl @Inject constructor(
    private val service: RemoteApi,
    private val database: SpaceRoomDatabase,
    private val preferences: SharedPreferences) : SyncLaunches {

    companion object {
        const val KEY_LAUNCHES_FETCHED_ALREADY = "launches-fetched-already"
    }

    private val mutex = Mutex()

    private val syncListeners: MutableList<SyncLaunches.SyncListener> = mutableListOf()

    @WorkerThread
    override suspend fun doSync(force: Boolean): Boolean {
        mutex.withLock {
            if (areLaunchesFetchedAlready() && !force) {
                return true
            }

            try {
                val response = service.timeline()
                if (response.responseCode == RESPONSE_CODE_BAD_API_KEY) {
                    val errorMessage = "bad api key response"
                    Timber.e(errorMessage)
                    throw IllegalStateException(errorMessage)
                } else {
                    preferences.edit().putBoolean(KEY_LAUNCHES_FETCHED_ALREADY, true).apply()

                    val launchSites = response.launchSites.orEmpty().map { it.toLaunchSiteEntity() }
                    val rockets = response.rockets.orEmpty().map { it.toRocketEntity() }
                    val launchesResponse = response.launches.orEmpty()
                    val launchAndTagEntities = launchesResponse.map {
                        LaunchAndTagsEntity(
                            it.toLaunchEntity(),
                            it.tags.map { it.type })
                    }
                    val tagEntities = launchAndTagEntities
                        .map {
                            it.tagTypes.map { tagType -> TagEntity(launchId = it.launch.id, type = tagType) }
                        }.flatten()
                    val photoEntities = response.photos.orEmpty().map { it.toPhotoEntity() }
                    val photoLaunchEntities = launchesResponse.map { remoteLaunch ->
                        remoteLaunch.photos.orEmpty().map { photoId -> PhotoLaunchEntity(photoId, remoteLaunch.id) }
                    }.flatten()

                    database.runInTransaction {
                        database.launchDao().clear()
                        database.launchSiteDao().insertAll(*launchSites.toTypedArray())
                        database.rocketDao().insertAll(*rockets.toTypedArray())
                        database.launchDao().insertAll(*launchAndTagEntities.map { it.launch }.toTypedArray())
                        database.tagDao().insertAll(*tagEntities.toTypedArray())
                        database.photoDao().insertAll(*photoEntities.toTypedArray())
                        database.photoDao().insertAll(*photoLaunchEntities.toTypedArray())
                    }

                    syncListeners.forEach {
                        it.onSync()
                    }
                }
            } catch (e: Exception) {
                Timber.e(e)
                return false
            }
            return true
        }
    }

    override fun addSyncListener(syncListener: SyncLaunches.SyncListener) {
        syncListeners.add(syncListener)
    }

    override fun removeSyncListener(syncListener: SyncLaunches.SyncListener) {
        syncListeners.remove(syncListener)
    }

    private fun areLaunchesFetchedAlready() = preferences.getBoolean(KEY_LAUNCHES_FETCHED_ALREADY, false)

}