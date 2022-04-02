package sk.kasper.database.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import sk.kasper.database.SpaceRoomDatabase
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun providesDatabase(@ApplicationContext context: Context): SpaceRoomDatabase {
        return Room.databaseBuilder(context, SpaceRoomDatabase::class.java, "car-database")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun providesCarDao(database: SpaceRoomDatabase) = database.carDao()

}