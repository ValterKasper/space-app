package sk.kasper.space.api

import retrofit2.http.GET
import sk.kasper.space.api.entity.RemoteLaunchesResponse


interface RemoteApi {

    @GET("timeline")
    suspend fun timeline(): RemoteLaunchesResponse

}

