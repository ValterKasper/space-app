package sk.kasper.remote.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import sk.kasper.base.Flags
import sk.kasper.base.SettingsManager
import sk.kasper.remote.ApiUtils.createRemoteApi
import sk.kasper.remote.FakeRemoteApi
import sk.kasper.remote.RemoteApi
import javax.inject.Provider
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
internal class RemoteApiModule {

    @Singleton
    @Provides
    internal fun providesRemoteApi(
        settingsManager: SettingsManager,
        fakeRemoteApi: Provider<FakeRemoteApi>,
        flags: Flags
    ): RemoteApi {
        return if (flags.bootstrapResponseApi) {
            fakeRemoteApi.get()
        } else {
            createRemoteApi(settingsManager, flags)
        }
    }

}