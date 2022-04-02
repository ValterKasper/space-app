package sk.kasper.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import sk.kasper.database.entity.Car

@Dao
interface CarDao {

    @Query("SELECT * FROM car")
    suspend fun getAll(): List<Car>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg tags: Car)

}