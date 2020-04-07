package sk.kasper.space.api

import android.content.Context
import com.google.gson.Gson
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Deferred
import org.threeten.bp.Duration
import org.threeten.bp.LocalDateTime
import org.threeten.bp.temporal.ChronoUnit
import sk.kasper.space.api.entity.RemoteLaunchesResponse
import sk.kasper.space.utils.readFileFromAssets
import sk.kasper.space.utils.toLocalDateTime
import sk.kasper.space.utils.toTimeStamp
import javax.inject.Inject
import javax.inject.Singleton

private const val RESPONSE_FILE_NAME = "fake_api_response.json"
private val BACK_TO_THE_FUTURE_OFFSET = Duration.of(4, ChronoUnit.HOURS)

@Singleton
class FakeRemoteApi @Inject constructor(private val context: Context) : RemoteApi {

    private val now = LocalDateTime.now()

    override fun timelineAsync(): Deferred<RemoteLaunchesResponse> {
        val originalResponse = Gson().fromJson(
                context.readFileFromAssets(RESPONSE_FILE_NAME),
                RemoteLaunchesResponse::class.java)

        // Back to the future ©
        val firstLaunchDateTime = originalResponse.launches!!.first().launchTs.toLocalDateTime()
        val firstLaunchDurationOffset = Duration.between(firstLaunchDateTime, now)
        val launchDurationOffset = firstLaunchDurationOffset.plus(BACK_TO_THE_FUTURE_OFFSET)
        val newResponse = originalResponse
                .copy(launches = originalResponse.launches.map { it.copy(launchTs = it.launchTs.toLocalDateTime().plus(launchDurationOffset).toTimeStamp()) })

        return  CompletableDeferred(newResponse)
    }

}