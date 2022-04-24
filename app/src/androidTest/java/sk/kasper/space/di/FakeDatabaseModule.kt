package sk.kasper.space.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import sk.kasper.database.SpaceDatabase
import sk.kasper.database.SpaceRoomDatabase
import sk.kasper.database.di.DatabaseModule
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DatabaseModule::class]
)
class FakeDatabaseModule {

    @Singleton
    @Provides
    internal fun providesSpaceRoomDatabase(@ApplicationContext context: Context): SpaceRoomDatabase {
        return Room.inMemoryDatabaseBuilder(context, SpaceRoomDatabase::class.java)
            .build()
    }

    @Provides
    internal fun providesRoomDatabase(database: SpaceRoomDatabase): SpaceDatabase = database

}
