package sk.kasper.database

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.google.common.truth.Truth.assertThat
import com.jakewharton.threetenabp.AndroidThreeTen
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.threeten.bp.LocalDateTime
import sk.kasper.database.dao.*
import sk.kasper.database.entity.*


@ExperimentalCoroutinesApi
class DatabaseTest {

    private lateinit var database: SpaceRoomDatabase
    private lateinit var launchDao: LaunchDao
    private lateinit var rocketDao: RocketDao
    private lateinit var launchSiteDao: LaunchSiteDao
    private lateinit var photoDao: PhotoDao
    private lateinit var tagDao: TagDao

    private companion object {

        const val LAUNCH_SITE_ID = 10L
        const val ROCKET_ID = 20L
        const val LAUNCH_SITE_NAME = "Launch site name"
        const val ROCKET_NAME = "Falcon"
        val LAUNCH_SITE_ENTITY = LaunchSiteEntity(LAUNCH_SITE_ID, LAUNCH_SITE_NAME, "Launch pad name", "url", 10.0, 15.0)
        val FALCON_CORE_ENTITY = FalconCoreEntity(true, 5, 0, "ADSP", "OCISLY")
        val LAUNCH_ENTITY = LaunchEntity(
            "30L", LocalDateTime.of(1969, 7, 16, 10, 59), "Zuma", "description",
            null, null, null, null, null, null,
            true, false, null, FALCON_CORE_ENTITY
        )
        val PHOTO_ENTITY = PhotoEntity(40L, "tURL", "fZURL", null, null)
        val ROCKET_ENTITY = RocketEntity(ROCKET_ID, ROCKET_NAME, 10.0f, 10.0f, 10.0f, 10.0f, 10.0f, 10.0f, 2, null)

    }

    @Before
    fun setUp() {
        val context = InstrumentationRegistry.getInstrumentation().context

        AndroidThreeTen.init(context)
        database = Room.inMemoryDatabaseBuilder(context, SpaceRoomDatabase::class.java).build()

        launchDao = database.launchDao()
        rocketDao = database.rocketDao()
        launchSiteDao = database.launchSiteDao()
        photoDao = database.photoDao()
        tagDao = database.tagDao()
    }

    @After
    fun closeDb() {
        database.close()
    }

    @Test
    fun insertLaunch_getLaunch() = runTest {
        rocketDao.insert(ROCKET_ENTITY)
        launchDao.insertAll(LAUNCH_ENTITY.copy(rocketId = ROCKET_ID))

        val loadedLaunch = launchDao.getLaunch(LAUNCH_ENTITY.id)

        LAUNCH_ENTITY.apply {
            assertThat(loadedLaunch.launchName).isEqualTo(launchName)
            assertThat(loadedLaunch.launchDateTime).isEqualTo(launchDateTime)
            assertThat(loadedLaunch.rocketName).isEqualTo(ROCKET_NAME)
            assertThat(loadedLaunch.videoUrl).isEqualTo(videoUrl)
            assertThat(loadedLaunch.description).isEqualTo(description)
        }

    }

    @Test
    fun insertLaunch_getLaunches() = runTest {
        rocketDao.insert(ROCKET_ENTITY)
        launchDao.insertAll(LAUNCH_ENTITY.copy(rocketId = ROCKET_ID))

        launchDao.observeLaunches().first().let {
            assertTrue(it.size == 1)
            val result = it.first()
            LAUNCH_ENTITY.apply {
                assertEquals(launchName, result.launchName)
                assertEquals(launchDateTime, result.launchDateTime)
                assertEquals(ROCKET_NAME, result.rocketName)
                assertEquals(ROCKET_ID, result.rocketId)
                assertEquals(accurateDate, result.accurateDate)
                assertEquals(accurateTime, result.accurateTime)
            }
        }
    }

    @Test
    fun insertLaunch_withFalconCore_getFalconCore() = runTest {
        rocketDao.insert(ROCKET_ENTITY)
        launchDao.insertAll(LAUNCH_ENTITY.copy(rocketId = ROCKET_ID))

        val falconCore = launchDao.getFalconCore(LAUNCH_ENTITY.id)

        assertEquals(LAUNCH_ENTITY.falconCore, falconCore)
    }

    @Test
    fun insertLaunch_withoutFalconCore_falconCoreIsNull() = runTest {
        rocketDao.insert(ROCKET_ENTITY)
        launchDao.insertAll(LAUNCH_ENTITY.copy(rocketId = ROCKET_ID, falconCore = null))

        val falconCore = launchDao.getFalconCore(LAUNCH_ENTITY.id)

        assertTrue(falconCore.isEmpty())
    }

    @Test
    fun insertLaunch_getOrbit() = runTest {
        rocketDao.insert(ROCKET_ENTITY)
        launchDao.insertAll(LAUNCH_ENTITY.copy(rocketId = ROCKET_ID, orbit = "MEO"))

        val orbit = launchDao.getOrbit(LAUNCH_ENTITY.id)

        assertEquals("MEO", orbit)
    }

    @Test
    fun insertLaunchTags() = runTest {
        val tagTypeA: Long = 10
        val tagTypeB: Long = 20

        launchDao.insertAll(LAUNCH_ENTITY)
        tagDao.insertAll(
            TagEntity(null, LAUNCH_ENTITY.id, tagTypeA),
            TagEntity(null, LAUNCH_ENTITY.id, tagTypeB)
        )

        launchDao.getLaunch(LAUNCH_ENTITY.id).let { it ->
            LAUNCH_ENTITY.apply {
                val launchTagTypes = it.tags.map { it.type }
                assertTrue(launchTagTypes.size == 2)
                assertTrue(launchTagTypes.contains(tagTypeA))
                assertTrue(launchTagTypes.contains(tagTypeB))
            }
        }
    }

    @Test
    fun getLaunchSiteByLaunchId() = runTest {
        launchSiteDao.insertAll(LAUNCH_SITE_ENTITY)
        launchDao.insertAll(LAUNCH_ENTITY.copy(launchSiteId = LAUNCH_SITE_ID))

        val launchSiteEntity = launchSiteDao.getLaunchSiteByLaunchId(LAUNCH_ENTITY.id)

        assertEquals(LAUNCH_SITE_ID, launchSiteEntity.id)
        assertEquals(LAUNCH_SITE_NAME, launchSiteEntity.launchSiteName)
    }

    @Test
    fun loadRocketByLaunchId() = runTest {
        rocketDao.insert(ROCKET_ENTITY)
        launchDao.insertAll(LAUNCH_ENTITY.copy(rocketId = ROCKET_ID))

        assertEquals(ROCKET_ENTITY, rocketDao.loadRocketByLaunchId(LAUNCH_ENTITY.id))
    }

    @Test
    fun launchPhotos() = runTest {
        launchDao.insertAll(LAUNCH_ENTITY)
        val firstPhotoEntity = PHOTO_ENTITY
        val secondPhotoEntity = PHOTO_ENTITY.copy(id = 99)
        val photoIds = photoDao.insertAll(firstPhotoEntity, secondPhotoEntity)

        photoDao.insertAll(
            PhotoLaunchEntity(photoIds[0], LAUNCH_ENTITY.id),
            PhotoLaunchEntity(photoIds[1], LAUNCH_ENTITY.id)
        )

        assertEquals(listOf(firstPhotoEntity, secondPhotoEntity), photoDao.loadLaunchPhotos(LAUNCH_ENTITY.id))
    }

}
