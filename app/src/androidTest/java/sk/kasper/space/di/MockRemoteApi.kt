package sk.kasper.space.di

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Deferred
import sk.kasper.space.api.RemoteApi
import sk.kasper.space.api.entity.RemoteLaunch
import sk.kasper.space.api.entity.RemoteLaunchesResponse
import javax.inject.Inject


/**
 * Dovoluje nastavit odpoved az potom, ako sa zaregistruje klient na listLaunches().
 */
class MockRemoteApi @Inject constructor(): RemoteApi {

    private var completableDeferred = CompletableDeferred<RemoteLaunchesResponse>()

    override fun timelineAsync(): Deferred<RemoteLaunchesResponse> {
        return completableDeferred
    }

    fun listLaunchesReturn(list: List<RemoteLaunch>) {
        completableDeferred.complete(RemoteLaunchesResponse(0, list, emptyList(), emptyList(), emptyList()))
    }

}