package sk.kasper.base.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import sk.kasper.base.*

@InstallIn(SingletonComponent::class)
@Module
internal class BaseModule {

    @Provides
    internal fun providesFlags(): Flags = FlagsImpl

    @Provides
    internal fun providesFileReader(fileReaderImpl: FileReaderImpl): FileReader = fileReaderImpl

    @Provides
    fun providesGoogleApiHelper(googleApiHelper: GoogleApiHelperImpl): GoogleApiHelper = googleApiHelper

}