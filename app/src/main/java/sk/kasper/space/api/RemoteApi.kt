package sk.kasper.space.api

import retrofit2.http.GET
import sk.kasper.space.api.entity.RemoteLaunchesResponse


// TODO D: move to own module
interface RemoteApi {

    @GET("timeline")
    suspend fun timeline(): RemoteLaunchesResponse

}

