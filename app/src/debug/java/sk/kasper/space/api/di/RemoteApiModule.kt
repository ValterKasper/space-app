package sk.kasper.space.api.di

import dagger.Lazy
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import sk.kasper.space.BuildConfig
import sk.kasper.space.api.ApiUtils.authInterceptor
import sk.kasper.space.api.ApiUtils.createRemoteApi
import sk.kasper.space.api.ApiUtils.loggingInterceptor
import sk.kasper.space.api.FakeRemoteApi
import sk.kasper.space.api.RemoteApi
import sk.kasper.ui_common.settings.SettingsManager
import timber.log.Timber
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