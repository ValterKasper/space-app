package sk.kasper.space.di

import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import sk.kasper.remote.RemoteApi
import sk.kasper.remote.di.RemoteApiModule
import sk.kasper.space.fake.FakeRemoteApi
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [RemoteApiModule::class]
)
abstract class FakeAnalyticsModule {

    @Singleton
    @Binds
    abstract fun bindFakeRemoteApi(
        fakeAnalyticsService: FakeRemoteApi
    ): RemoteApi
}
