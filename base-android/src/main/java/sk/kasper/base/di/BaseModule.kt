package sk.kasper.base.di

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import sk.kasper.base.*
import sk.kasper.base.init.*
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
internal class BaseModule {

    @Provides
    fun providesFlags(): Flags = FlagsImpl

    @Provides
    fun providesFileReader(fileReaderImpl: FileReaderImpl): FileReader = fileReaderImpl

    @Provides
    fun providesGoogleApiHelper(googleApiHelper: GoogleApiHelperImpl): GoogleApiHelper = googleApiHelper

    @Singleton
    @Provides
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(context)
    }

    @Singleton
    @Provides
    @AppCoroutineScope
    fun providesAppCoroutineScope(): CoroutineScope = MainScope()


    @Provides
    @IntoSet
    fun providesNightModeInitializer(initializer: NightModeInitializer): AppInitializer = initializer

    @Provides
    @IntoSet
    fun providesLoggingInitializer(initializer: LoggingInitializer): AppInitializer = initializer

    @Provides
    @IntoSet
    fun providesCrashlyticsInitializer(initializer: CrashlyticsInitializer): AppInitializer = initializer

    @Provides
    @IntoSet
    fun providesJavaTimeInitializer(initializer: JavaTimeInitializer): AppInitializer = initializer

}