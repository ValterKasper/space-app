package sk.kasper.remote

import com.google.gson.Gson
import org.threeten.bp.Duration
import org.threeten.bp.LocalDateTime
import org.threeten.bp.temporal.ChronoUnit
import sk.kasper.base.FileReader
import sk.kasper.base.Flags
import sk.kasper.base.utils.toLocalDateTime
import sk.kasper.base.utils.toTimeStamp
import sk.kasper.remote.entity.RemoteLaunchesResponse
import javax.inject.Inject

internal class DemoRemoteApi @Inject constructor(private val fileReader: FileReader, private val flags: Flags) :
    RemoteApi {

    companion object {
        private val BACK_TO_THE_FUTURE_OFFSET = Duration.of(4, ChronoUnit.HOURS)
    }

    private val now = LocalDateTime.now()

    override suspend fun timeline(): RemoteLaunchesResponse {
        val originalResponse = Gson().fromJson(
            fileReader.readFileFromAssets(flags.bootstrapResponseApiFileName),
            RemoteLaunchesResponse::class.java
        )

        // Back to the future ©
        val launches = originalResponse.launches!!
        val firstLaunchDateTime = launches.first().launchTs.toLocalDateTime()
        val firstLaunchDurationOffset = Duration.between(firstLaunchDateTime, now)
        val launchDurationOffset = firstLaunchDurationOffset.plus(BACK_TO_THE_FUTURE_OFFSET)

        return originalResponse
            .copy(launches = launches.map {
                it.copy(
                    launchTs = it.launchTs.toLocalDateTime().plus(launchDurationOffset).toTimeStamp()
                )
            })
    }

}