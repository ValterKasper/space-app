package sk.kasper.base.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import sk.kasper.base.FileReader
import sk.kasper.base.FileReaderImpl
import sk.kasper.base.Flags
import sk.kasper.base.FlagsImpl

@InstallIn(SingletonComponent::class)
@Module
class BaseModule {

    @Provides
    internal fun providesFlags(): Flags = FlagsImpl

    @Provides
    internal fun providesFileReader(fileReaderImpl: FileReaderImpl): FileReader = fileReaderImpl

}