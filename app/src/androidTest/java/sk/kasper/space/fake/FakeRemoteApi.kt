package sk.kasper.space.fake

import sk.kasper.base.logger.Logger
import sk.kasper.entity.Tag
import sk.kasper.remote.RemoteApi
import sk.kasper.remote.entity.RemoteLaunch
import sk.kasper.remote.entity.RemoteLaunchesResponse
import sk.kasper.remote.entity.RemoteTag
import sk.kasper.space.robot.droid.LaunchDroid
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class FakeRemoteApi @Inject constructor() : RemoteApi {

    override suspend fun timeline(): RemoteLaunchesResponse {
        return RemoteLaunchesResponse(
            0,
            mapToRemoteLaunches(listOf(LaunchDroid("l1", listOf(Tag.CUBE_SAT)))),
            emptyList(),
            emptyList(),
            emptyList()
        )
    }

    private fun mapToRemoteLaunches(droidLaunches: List<LaunchDroid>): List<RemoteLaunch> {
        droidLaunches.forEach {
            Logger.d("fromServerReturnLaunches $it")
        }

        val launches = droidLaunches.mapIndexed { index: Int, launchDroid: LaunchDroid ->
            RemoteLaunch(
                index.toString(),
                System.currentTimeMillis() + TimeUnit.DAYS.toMillis(index.toLong() + 1),
                launchDroid.name,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                true,
                true,
                null,
                null,
                launchDroid.tags.map { RemoteTag(it) },
                null
            )
        }
        return launches
    }


}