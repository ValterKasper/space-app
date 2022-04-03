package sk.kasper.space.api.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import sk.kasper.base.Flags
import sk.kasper.base.SettingsManager
import sk.kasper.remote.ApiUtils.createRemoteApi
import sk.kasper.remote.RemoteApi
import javax.inject.Provider
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class RemoteApiModule {

    @Singleton
    @Provides
    fun providesRemoteApi(
        settingsManager: SettingsManager,
        fakeRemoteApi: Provider<FakeRemoteApi>,
        flags: Flags
    ): RemoteApi {
        return if (flags.bootstrapResponseApi) {
            createRemoteApi(settingsManager, flags)
        } else {
            fakeRemoteApi.get()
        }
    }

}