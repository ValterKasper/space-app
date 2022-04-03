package sk.kasper.repository.impl

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import sk.kasper.base.SettingKey
import sk.kasper.base.SettingsManager
import sk.kasper.base.logger.Logger
import sk.kasper.database.SpaceDatabase
import sk.kasper.database.entity.LaunchAndTagsEntity
import sk.kasper.database.entity.PhotoLaunchEntity
import sk.kasper.database.entity.TagEntity
import sk.kasper.remote.RemoteApi
import sk.kasper.remote.entity.RESPONSE_CODE_BAD_API_KEY
import sk.kasper.repository.SyncLaunchesRepository
import javax.inject.Inject

internal class SyncLaunchesRepositoryImpl @Inject constructor(
    private val service: RemoteApi,
    private val database: SpaceDatabase,
    private val settingsManager: SettingsManager
) : SyncLaunchesRepository {

    private val mutex = Mutex()

    private val syncListeners: MutableList<SyncLaunchesRepository.SyncListener> = mutableListOf()

    override suspend fun doSync(force: Boolean): Boolean {
        mutex.withLock {
            if (areLaunchesFetchedAlready() && !force) {
                return true
            }

            try {
                val response = service.timeline()
                if (response.responseCode == RESPONSE_CODE_BAD_API_KEY) {
                    val errorMessage = "bad api key response"
                    Logger.loge("SYNC", errorMessage, null)
                    throw IllegalStateException(errorMessage)
                } else {
                    settingsManager.setBoolean(SettingKey.LAUNCHES_FETCHED_ALREADY, true)

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

                    // TODO D: revert
                    // database.runInTransaction {
                    database.launchDao().clear()
                    database.launchSiteDao().insertAll(*launchSites.toTypedArray())
                    database.rocketDao().insertAll(*rockets.toTypedArray())
                    database.launchDao().insertAll(*launchAndTagEntities.map { it.launch }.toTypedArray())
                    database.tagDao().insertAll(*tagEntities.toTypedArray())
                    database.photoDao().insertAll(*photoEntities.toTypedArray())
                    database.photoDao().insertAll(*photoLaunchEntities.toTypedArray())
//                    }

                    syncListeners.forEach {
                        it.onSync()
                    }
                }
            } catch (e: Exception) {
                Logger.loge("SYNC", "", e)
                return false
            }
            return true
        }
    }

    override fun addSyncListener(syncListener: SyncLaunchesRepository.SyncListener) {
        syncListeners.add(syncListener)
    }

    override fun removeSyncListener(syncListener: SyncLaunchesRepository.SyncListener) {
        syncListeners.remove(syncListener)
    }

    private fun areLaunchesFetchedAlready() = settingsManager.getBoolean(SettingKey.LAUNCHES_FETCHED_ALREADY)

}