package sk.kasper.space.api

import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import sk.kasper.space.api.entity.RemoteLaunchesResponse


interface RemoteApi {

    @GET("timeline")
    fun timelineAsync(): Deferred<RemoteLaunchesResponse>

}

