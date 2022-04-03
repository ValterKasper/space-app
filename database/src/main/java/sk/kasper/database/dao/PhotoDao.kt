package sk.kasper.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import sk.kasper.database.entity.PhotoEntity
import sk.kasper.database.entity.PhotoLaunchEntity

@Dao
interface PhotoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg photoEntity: PhotoEntity): List<Long>

    @Insert
    suspend fun insertAll(vararg photoLaunchEntity: PhotoLaunchEntity)

    @Query(
            """
                SELECT
                    id,
                    thumbnailUrl,
                    fullSizeUrl,
                    sourceName,
                    description
                FROM
                    photo
                INNER JOIN
                    photo_launch ON photo.id=photo_launch.photoId
                WHERE
                    photo_launch.launchId=:launchId
                    """)
    suspend fun loadLaunchPhotos(launchId: String): List<PhotoEntity>

}