package sk.kasper.remote

import retrofit2.http.GET
import sk.kasper.remote.entity.RemoteLaunchesResponse


interface RemoteApi {

    @GET("timeline")
    suspend fun timeline(): RemoteLaunchesResponse

}

