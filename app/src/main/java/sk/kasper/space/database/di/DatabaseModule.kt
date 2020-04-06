package sk.kasper.space.database.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import sk.kasper.space.database.Database
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun providesDatabase(context: Context): Database {
        return Room.databaseBuilder(context, Database::class.java, "local-database")
                .fallbackToDestructiveMigration()
                .build()
    }

}