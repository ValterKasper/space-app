package sk.kasper.remote.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import sk.kasper.base.Flags
import sk.kasper.base.SettingsManager
import sk.kasper.remote.ApiUtils.createRemoteApi
import sk.kasper.remote.DemoRemoteApi
import sk.kasper.remote.RemoteApi
import javax.inject.Provider
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class RemoteApiModule {

    @Singleton
    @Provides
    internal fun providesRemoteApi(
        settingsManager: SettingsManager,
        demoRemoteApi: Provider<DemoRemoteApi>,
        flags: Flags
    ): RemoteApi {
        return if (flags.bootstrapResponseApi) {
            demoRemoteApi.get()
        } else {
            createRemoteApi(settingsManager, flags)
        }
    }

}