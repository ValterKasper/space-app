package sk.kasper.space.api.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import sk.kasper.space.api.ApiUtils.createRemoteApi
import sk.kasper.space.api.RemoteApi
import sk.kasper.ui_common.settings.SettingsManager
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class RemoteApiModule {

    @Singleton
    @Provides
    fun providesRemoteApi(settingsManager: SettingsManager): RemoteApi {
        return createRemoteApi(settingsManager)
    }

}