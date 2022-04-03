package sk.kasper.base.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import sk.kasper.base.Flags
import sk.kasper.base.FlagsImpl

@InstallIn(SingletonComponent::class)
@Module
class BaseModule {

    @Provides
    fun providesFlags(): Flags = FlagsImpl

}