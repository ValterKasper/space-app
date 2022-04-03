package sk.kasper.database.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import sk.kasper.database.entity.FalconCoreEntity
import sk.kasper.database.entity.LaunchDetailEntity
import sk.kasper.database.entity.LaunchEntity

@Dao
abstract class LaunchDao {

    @Transaction
    @Query("""
        SELECT
            launch.id,
            launch.launchName,
            launch.launchTs,
            launch.rocketId,
            launch.accurateDate,
            launch.accurateTime,
            launch.description,
            launch.mainPhotoUrl,
            launch.hashTag,
            launch.payloadMass,
            launch.videoUrl,
            rocket.rocketName,
            manufacturer.manufacturerName
        FROM
            launch
        LEFT JOIN
            rocket ON launch.rocketId = rocket.id
        LEFT JOIN
            manufacturer ON rocket.manufacturerId = manufacturer.id
        ORDER BY
            launch.launchTs
        """
    )
    abstract fun observeLaunches(): Flow<List<LaunchDetailEntity>>

    @Transaction
    @Query("""
        SELECT
            launch.id,
            launch.launchName,
            launch.launchTs,
            launch.rocketId,
            launch.accurateDate,
            launch.accurateTime,
            launch.videoUrl,
            launch.description,
            launch.mainPhotoUrl,
            launch.hashTag,
            launch.payloadMass,
            rocket.rocketName,
            manufacturer.manufacturerName
        FROM
            launch
        LEFT JOIN
            rocket ON launch.rocketId = rocket.id
        LEFT JOIN
            manufacturer ON rocket.manufacturerId = manufacturer.id
        WHERE
            :launchId = launch.id
            """)
    abstract fun getLaunch(launchId: String): LaunchDetailEntity

    @Query(
       """
        SELECT
            launch.falconCore_reused AS reused,
            launch.falconCore_block AS block,
            launch.falconCore_flights AS flights,
            launch.falconCore_landingType AS landingType,
            launch.falconCore_landingVehicle AS landingVehicle
        FROM
            launch
        WHERE
            :launchId = launch.id
        """)
    abstract suspend fun getFalconCore(launchId: String): FalconCoreEntity

    @Query("""
        SELECT
            launch.orbit
        FROM
            launch
        WHERE
            :launchId = launch.id
    """)
    abstract fun getOrbit(launchId: String): String?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertAll(vararg launches: LaunchEntity)

    @Query("DELETE FROM launch")
    abstract suspend fun clear()

}

