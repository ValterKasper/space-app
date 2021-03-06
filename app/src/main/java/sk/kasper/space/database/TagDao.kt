package sk.kasper.space.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import sk.kasper.space.database.entity.TagEntity

@Dao
interface TagDao {

    @Query("SELECT * FROM tag")
    suspend fun getTags(): List<TagEntity>

    @Query("SELECT * FROM tag WHERE launchId = :launchId")
    suspend fun getTagsByLaunch(launchId: String): List<TagEntity>

    @Query("SELECT * FROM tag WHERE type = :type")
    suspend fun getTagsByType(type: Int): List<TagEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg tags: TagEntity)

}