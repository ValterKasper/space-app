package sk.kasper.space.sync

import android.content.SharedPreferences
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito.eq
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner
import sk.kasper.domain.model.Tag
import sk.kasper.space.api.RemoteApi
import sk.kasper.space.api.entity.*
import sk.kasper.space.database.*
import sk.kasper.space.database.entity.PhotoEntity
import sk.kasper.space.database.entity.PhotoLaunchEntity
import sk.kasper.space.database.entity.TagEntity
import sk.kasper.space.notification.showLaunchNotificationJob.LaunchNotificationChecker
import sk.kasper.space.sync.SyncLaunchesImpl.Companion.KEY_LAUNCHES_FETCHED_ALREADY

private const val LAUNCH_SITE_ID = 1L

@RunWith(MockitoJUnitRunner::class)
class SyncLaunchesImplTest {

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
    private lateinit var sharedPreferences: SharedPreferences

    @Mock
    private lateinit var editor: SharedPreferences.Editor

    @Mock
    private lateinit var database: Database

    @Mock
    private lateinit var launchNotificationChecker: LaunchNotificationChecker

    private lateinit var syncLaunches: SyncLaunchesImpl

    @Before
    fun setUp() {
        whenever(database.launchDao()).thenReturn(launchDao)
        whenever(database.launchSiteDao()).thenReturn(launchSiteDao)
        whenever(database.photoDao()).thenReturn(photoDao)
        whenever(database.tagDao()).thenReturn(tagDao)
        whenever(database.rocketDao()).thenReturn(rocketDao)

        syncLaunches = SyncLaunchesImpl(remoteApi, database, sharedPreferences)

        whenever(sharedPreferences.edit()).thenReturn(editor)
        whenever(editor.putBoolean(ArgumentMatchers.eq(KEY_LAUNCHES_FETCHED_ALREADY), eq(true))).thenReturn(editor)
    }

    @Test
    fun doSync_soft_fetchedAlready_justComplete() = runBlocking {
        whenever(sharedPreferences.getBoolean(KEY_LAUNCHES_FETCHED_ALREADY, false)).thenReturn(true)

        assertTrue(syncLaunches.doSync(false))

        verifyNoMoreInteractions(remoteApi, launchDao, launchSiteDao, launchNotificationChecker)
    }

    @Test
    fun doSync_force_synchronize() = runBlocking {
        val photoId: Long = 89
        val launchId: Long = 1
        val remoteFalconInfo = RemoteFalconInfo(listOf(RemoteFalconCore(true, 5, 1, RemoteSpaceXLandingType("Ocean"), RemoteSpaceXLandingVehicle("RTB"))))
        whenever(remoteApi.timeline()).thenReturn(RemoteLaunchesResponse(
                0,
                listOf(RemoteLaunch(launchId, 321, "Launch", null, null, null, null, null, null, null, true, true, LAUNCH_SITE_ID, listOf(photoId), listOf(RemoteTag(Tag.ISS)), remoteFalconInfo)),
                listOf(RemoteLaunchSite(LAUNCH_SITE_ID, "Site", "Pad", "URL", 10.0, 12.0)),
                listOf(RemoteRocket(1L, "", 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1, null)),
                listOf(RemotePhoto(photoId, "ThumbnailSizeUrl", "FullSizeUrl"))))
        whenever(launchDao.insertAll(any())).thenReturn(listOf(launchId))

        assertTrue(syncLaunches.doSync(true))

        argumentCaptor<Runnable>().apply {
            verify(database).runInTransaction(capture())
            firstValue.run()
        }

        verify(editor).putBoolean(KEY_LAUNCHES_FETCHED_ALREADY, true)
        verify(launchSiteDao).insertAll(any())
        verify(rocketDao).insertAll(any())
        verify(launchDao).insertAll(any())
        verify(tagDao).insertAll(TagEntity(null, launchId, Tag.ISS))
        verify(photoDao).insertAll(PhotoEntity(photoId, "ThumbnailSizeUrl", "FullSizeUrl", null, null))
        verify(photoDao).insertAll(PhotoLaunchEntity(photoId, launchId))
    }

    @Test
    fun doSync_force_serverError() = runBlocking {
        whenever(remoteApi.timeline()).thenThrow(RuntimeException())

        assertFalse(syncLaunches.doSync(true))
    }

}