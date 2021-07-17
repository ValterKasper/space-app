package sk.kasper.space.api

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import sk.kasper.space.BuildConfig
import sk.kasper.ui_common.settings.SettingsManager
import timber.log.Timber

object ApiUtils {

    private val authInterceptor = Interceptor { chain ->
        val newUrl = chain.request().url
            .newBuilder()
            .addQueryParameter("apiKey", BuildConfig.API_KEY)
            .build()

        val newRequest = chain.request()
            .newBuilder()
            .url(newUrl)
            .build()

        chain.proceed(newRequest)
    }

    private val loggingInterceptor = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
        override fun log(message: String) {
            Timber.tag("OkHttp").d(message)
        }
    }).apply {
        level = HttpLoggingInterceptor.Level.BASIC
    }

    fun createRemoteApi(settingsManager: SettingsManager): RemoteApi {
        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(authInterceptor)
            .build()

        val apiEndpoint = settingsManager.apiEndpoint
        if (!BuildConfig.DEBUG && apiEndpoint != SettingsManager.PRODUCTION) {
            throw IllegalStateException("Connecting to non production server in release build")
        }

        val retrofit = Retrofit.Builder()
            .client(client)
            .baseUrl(
                when (apiEndpoint) {
                    SettingsManager.PRODUCTION -> "https://li807-27.members.linode.com:8443/"
                    SettingsManager.LOCALHOST -> "http://10.0.2.2:8080/"
                    SettingsManager.RASPBERRY -> "http://10.0.0.2:8080/"
                    else -> throw IllegalStateException()
                }
            )
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(RemoteApi::class.java)
    }

}