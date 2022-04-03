package sk.kasper.base.di

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import sk.kasper.base.*
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
internal class BaseModule {

    @Provides
    internal fun providesFlags(): Flags = FlagsImpl

    @Provides
    internal fun providesFileReader(fileReaderImpl: FileReaderImpl): FileReader = fileReaderImpl

    @Provides
    fun providesGoogleApiHelper(googleApiHelper: GoogleApiHelperImpl): GoogleApiHelper = googleApiHelper

    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(context)
    }

}