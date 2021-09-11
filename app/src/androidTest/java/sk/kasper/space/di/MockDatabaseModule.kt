package sk.kasper.space.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import sk.kasper.space.database.Database
import javax.inject.Singleton

//@Module
//class MockDatabaseModule {
//
//    @Singleton
//    @Provides
//    fun providesDatabase(context: Context): Database {
//        return Room.inMemoryDatabaseBuilder(context, Database::class.java).build()
//    }
//}