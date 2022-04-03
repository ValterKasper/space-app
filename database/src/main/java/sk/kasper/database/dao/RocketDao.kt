package sk.kasper.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import sk.kasper.database.entity.RocketEntity

@Dao
interface RocketDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(rocket: RocketEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg rocketEntity: RocketEntity): List<Long>

    @Query("SELECT * FROM rocket")
    fun loadRockets(): List<RocketEntity>

    @Query("SELECT * FROM rocket WHERE id = :id")
    suspend fun loadRocket(id: Long): RocketEntity

    @Query("SELECT * FROM rocket")
    fun loadRocketsSync(): List<RocketEntity>

    @Query("""
        SELECT    
            rocket.id,    
            rocket.rocketName,    
            rocket.height,    
            rocket.diameter,    
            rocket.mass,    
            rocket.payloadLeo,    
            rocket.payloadGto,    
            rocket.thrust,    
            rocket.stages,    
            rocket.manufacturerId 
        FROM    
            launch 
        LEFT JOIN    
            rocket 
        ON    
            launch.rocketId = rocket.id 
        WHERE    
            launch.id = :launchId
        """)
    suspend fun loadRocketByLaunchId(launchId: String): RocketEntity

}