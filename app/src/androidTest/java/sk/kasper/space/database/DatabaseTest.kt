package sk.kasper.space.database

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import sk.kasper.space.database.entity.*
/*

@RunWith(AndroidJUnit4::class)
class DatabaseTest {

    private lateinit var database: Database
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
        val LAUNCH_ENTITY = LaunchEntity(30L, 42, "Zuma", "description",
                null, null, null, null, null, null,
                true, false, null, FALCON_CORE_ENTITY)
        val PHOTO_ENTITY = PhotoEntity(40L, "tURL", "fZURL", null, null)
        val ROCKET_ENTITY = RocketEntity(ROCKET_ID, ROCKET_NAME, 10.0f, 10.0f, 10.0f, 10.0f, 10.0f, 10.0f, 2, null)

    }

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getInstrumentation().context,
                Database::class.java).build()

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
    fun insertLaunch_getLaunch() {
        rocketDao.insert(ROCKET_ENTITY)
        val launchId = launchDao.insertAll(LAUNCH_ENTITY.copy(rocketId = ROCKET_ID))[0]

        val loadedLaunch = launchDao.getLaunch(launchId)

        LAUNCH_ENTITY.apply {
            assertEquals(launchName, loadedLaunch.launchName)
            assertEquals(launchTs, loadedLaunch.launchTs)
            assertEquals(ROCKET_NAME, loadedLaunch.rocketName)
            assertEquals(videoUrl, loadedLaunch.videoUrl)
            assertEquals(description, loadedLaunch.description)
        }

    }

    @Test
    fun insertLaunch_getLaunches() = runBlocking {
        rocketDao.insert(ROCKET_ENTITY)
        launchDao.insertAll(LAUNCH_ENTITY.copy(rocketId = ROCKET_ID))[0]

        launchDao.getLaunches().let {
            assertTrue(it.size == 1)
            val result = it.first()
            LAUNCH_ENTITY.apply {
                assertEquals(launchName, result.launchName)
                assertEquals(launchTs, result.launchTs)
                assertEquals(ROCKET_NAME, result.rocketName)
                assertEquals(ROCKET_ID, result.rocketId)
                assertEquals(accurateDate, result.accurateDate)
                assertEquals(accurateTime, result.accurateTime)
            }
        }

        Unit
    }

    @Test
    fun insertLaunch_withFalconCore_getFalconCore() = runBlocking {
        rocketDao.insert(ROCKET_ENTITY)
        val launchId = launchDao.insertAll(LAUNCH_ENTITY.copy(rocketId = ROCKET_ID))[0]

        val falconCore = launchDao.getFalconCore(launchId)

        assertEquals(LAUNCH_ENTITY.falconCore, falconCore)
    }

    @Test
    fun insertLaunch_withoutFalconCore_falconCoreIsNull() = runBlocking {
        rocketDao.insert(ROCKET_ENTITY)
        val launchId = launchDao.insertAll(LAUNCH_ENTITY.copy(rocketId = ROCKET_ID, falconCore = null))[0]

        val falconCore = launchDao.getFalconCore(launchId)

        assertTrue(falconCore.isEmpty())
    }

    @Test
    fun insertLaunch_getOrbit() {
        rocketDao.insert(ROCKET_ENTITY)
        val launchId = launchDao.insertAll(LAUNCH_ENTITY.copy(rocketId = ROCKET_ID, orbit = "MEO"))[0]

        val orbit = launchDao.getOrbit(launchId)

        assertEquals("MEO", orbit)
    }

    @Test
    fun insertLaunchTags() {
        val tagTypeA: Long = 10
        val tagTypeB: Long = 20

        val launchId = launchDao.insertAll(LAUNCH_ENTITY)[0]
        tagDao.insertAll(
                TagEntity(null, launchId, tagTypeA),
                TagEntity(null, launchId, tagTypeB))

        launchDao.getLaunch(launchId).let { it ->
            LAUNCH_ENTITY.apply {
                val launchTagTypes = it.tags.map { it.type }
                assertTrue(launchTagTypes.size == 2)
                assertTrue(launchTagTypes.contains(tagTypeA))
                assertTrue(launchTagTypes.contains(tagTypeB))
            }
        }
    }

    @Test
    fun getLaunchSiteByLaunchId() = runBlocking {
        launchSiteDao.insertAll(LAUNCH_SITE_ENTITY)
        val launchId = launchDao.insertAll(LAUNCH_ENTITY.copy(launchSiteId = LAUNCH_SITE_ID))[0]

        val launchSiteEntity = launchSiteDao.getLaunchSiteByLaunchId(launchId)

        assertEquals(LAUNCH_SITE_ID, launchSiteEntity.id)
        assertEquals(LAUNCH_SITE_NAME, launchSiteEntity.launchSiteName)
    }

    @Test
    fun loadRocketByLaunchId() = runBlocking {
        rocketDao.insert(ROCKET_ENTITY)
        val launchId = launchDao.insertAll(LAUNCH_ENTITY.copy(rocketId = ROCKET_ID))[0]

        assertEquals(ROCKET_ENTITY, rocketDao.loadRocketByLaunchId(launchId))
    }

    @Test
    fun launchPhotos() = runBlocking {
        val launchId = launchDao.insertAll(LAUNCH_ENTITY).first()
        val firstPhotoEntity = PHOTO_ENTITY
        val secondPhotoEntity = PHOTO_ENTITY.copy(id = 99)
        val photoIds = photoDao.insertAll(firstPhotoEntity, secondPhotoEntity)

        photoDao.insertAll(
                PhotoLaunchEntity(photoIds[0], launchId),
                PhotoLaunchEntity(photoIds[1], launchId))

                assertEquals(listOf(firstPhotoEntity, secondPhotoEntity), photoDao.loadLaunchPhotos(launchId))
    }

}*/
