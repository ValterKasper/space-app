package sk.kasper.repository.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import sk.kasper.repository.RocketRepository2
import sk.kasper.repository.impl.RocketRepositoryImpl2

@InstallIn(SingletonComponent::class)
@Module
class RepositoryModule {

    @Provides
    internal fun providesRocketRepository2(rocketRepositoryImpl: RocketRepositoryImpl2): RocketRepository2 =
        rocketRepositoryImpl

}