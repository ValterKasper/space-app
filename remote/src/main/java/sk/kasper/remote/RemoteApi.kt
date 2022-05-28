package sk.kasper.remote

import retrofit2.http.GET
import sk.kasper.remote.entity.RemoteLaunchesResponse


interface RemoteApi {

    @GET("api/launches")
    suspend fun timeline(): RemoteLaunchesResponse

}

