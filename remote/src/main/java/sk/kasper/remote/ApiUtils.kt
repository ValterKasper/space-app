package sk.kasper.remote

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import sk.kasper.base.Flags
import sk.kasper.base.SettingsManager
import sk.kasper.base.logger.Logger

object ApiUtils {

    internal fun createRemoteApi(settingsManager: SettingsManager, flags: Flags): RemoteApi {
        val authInterceptor = Interceptor { chain ->
            val urlBuilder = chain.request().url.newBuilder()
            if (flags.apiKey.isNotEmpty()) {
                urlBuilder.addQueryParameter("apiKey", flags.apiKey)
            }

            val newRequest = chain.request()
                .newBuilder()
                .url(urlBuilder.build())
                .build()

            chain.proceed(newRequest)
        }

        val loggingInterceptor = HttpLoggingInterceptor {
            Logger.tag("OkHttp").d(it)
        }.apply {
            level = if (flags.isDebug)
                HttpLoggingInterceptor.Level.BODY
            else
                HttpLoggingInterceptor.Level.NONE
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(authInterceptor)
            .build()

        val apiEndpoint = settingsManager.apiEndpoint
        if (!flags.isDebug && apiEndpoint != SettingsManager.PRODUCTION) {
            throw IllegalStateException("Connecting to non production server in release build")
        }

        val retrofit = Retrofit.Builder()
            .client(client)
            .baseUrl(
                when (apiEndpoint) {
                    SettingsManager.PRODUCTION -> "https://planetescape.app/"
                    SettingsManager.LOCALHOST -> "http://10.0.2.2:5000/"
                    SettingsManager.RASPBERRY -> "http://10.0.0.2:8080/"
                    else -> throw IllegalStateException()
                }
            )
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(RemoteApi::class.java)
    }

}