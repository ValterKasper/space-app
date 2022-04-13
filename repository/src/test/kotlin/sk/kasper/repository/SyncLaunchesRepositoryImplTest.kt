package sk.kasper.repository

import com.nhaarman.mockitokotlin2.anyVararg
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner
import sk.kasper.base.SettingKey
import sk.kasper.base.SettingsManager
import sk.kasper.database.SpaceDatabase
import sk.kasper.database.dao.*
import sk.kasper.database.entity.PhotoEntity
import sk.kasper.database.entity.PhotoLaunchEntity
import sk.kasper.database.entity.TagEntity
import sk.kasper.entity.Tag
import sk.kasper.remote.RemoteApi
import sk.kasper.remote.entity.*
import sk.kasper.repository.impl.SyncLaunchesRepositoryImpl


@RunWith(MockitoJUnitRunner::class)
class SyncLaunchesRepositoryImplTest {

    companion object {
        private const val LAUNCH_SITE_ID = 1L
    }

    @Mock
    private lateinit var launchDao: LaunchDao

    @Mock
    private lateinit var launchSiteDao: LaunchSiteDao

    @Mock
    private lateinit var photoDao: PhotoDao

    @Mock
    private lateinit var tagDao: TagDao

    @Mock
    private lateinit var rocketDao: RocketDao

    @Mock
    private lateinit var remoteApi: RemoteApi

    @Mock
    private lateinit var settingsManager: SettingsManager

    @Mock
    private lateinit var database: SpaceDatabase

    private lateinit var syncLaunches: SyncLaunchesRepositoryImpl

    @Before
    fun setUp() {
        whenever(database.launchDao()).thenReturn(launchDao)
        whenever(database.launchSiteDao()).thenReturn(launchSiteDao)
        whenever(database.photoDao()).thenReturn(photoDao)
        whenever(database.tagDao()).thenReturn(tagDao)
        whenever(database.rocketDao()).thenReturn(rocketDao)

        syncLaunches = SyncLaunchesRepositoryImpl(remoteApi, database, settingsManager, { it() })
    }

    @Test
    fun `when launches are already fetched and force is not set then do nothing`() = runBlocking {
        whenever(settingsManager.getBoolean(SettingKey.LAUNCHES_FETCHED_ALREADY)).thenReturn(true)

        assertTrue(syncLaunches.doSync(false))

        verifyNoMoreInteractions(remoteApi, launchDao, launchSiteDao)
    }

    @Test
    fun `when launches are already fetched and force is set then synchronize`() = runBlocking {
        val photoId: Long = 89
        val launchId = "id"
        val remoteFalconInfo = RemoteFalconInfo(
            listOf(
                RemoteFalconCore(
                    true,
                    5,
                    1,
                    RemoteSpaceXLandingType("Ocean"),
                    RemoteSpaceXLandingVehicle("RTB")
                )
            )
        )
        whenever(remoteApi.timeline()).thenReturn(
            RemoteLaunchesResponse(
                0,
                listOf(
                    RemoteLaunch(
                        launchId,
                        321,
                        "Launch",
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        true,
                        true,
                        LAUNCH_SITE_ID,
                        listOf(photoId),
                        listOf(
                            RemoteTag(
                                Tag.ISS
                            )
                        ),
                        remoteFalconInfo
                    )
                ),
                listOf(RemoteLaunchSite(LAUNCH_SITE_ID, "Site", "Pad", "URL", 10.0, 12.0)),
                listOf(RemoteRocket(1L, "", 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1, null)),
                listOf(RemotePhoto(photoId, "ThumbnailSizeUrl", "FullSizeUrl"))
            )
        )
        whenever(settingsManager.getBoolean(SettingKey.LAUNCHES_FETCHED_ALREADY)).thenReturn(true)

        assertTrue(syncLaunches.doSync(true))

        verify(settingsManager).setBoolean(SettingKey.LAUNCHES_FETCHED_ALREADY, true)
        verify(launchSiteDao).insertAll(anyVararg())
        verify(rocketDao).insertAll(anyVararg())
        verify(launchDao).insertAll(anyVararg())
        verify(tagDao).insertAll(TagEntity(null, launchId, Tag.ISS))
        verify(photoDao).insertAll(PhotoEntity(photoId, "ThumbnailSizeUrl", "FullSizeUrl", null, null))
        verify(photoDao).insertAll(PhotoLaunchEntity(photoId, launchId))
    }

    @Test
    fun `when api throws exception then return false`() = runBlocking {
        whenever(remoteApi.timeline()).thenThrow(RuntimeException())

        assertFalse(syncLaunches.doSync(true))
    }

}