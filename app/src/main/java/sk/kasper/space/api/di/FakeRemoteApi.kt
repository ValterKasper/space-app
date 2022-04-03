package sk.kasper.space.api.di

import android.content.Context
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import org.threeten.bp.Duration
import org.threeten.bp.LocalDateTime
import org.threeten.bp.temporal.ChronoUnit
import sk.kasper.base.Flags
import sk.kasper.entity.utils.toLocalDateTime
import sk.kasper.entity.utils.toTimeStamp
import sk.kasper.remote.RemoteApi
import sk.kasper.remote.entity.RemoteLaunchesResponse
import sk.kasper.space.utils.readFileFromAssets
import javax.inject.Inject
import javax.inject.Singleton

private val BACK_TO_THE_FUTURE_OFFSET = Duration.of(4, ChronoUnit.HOURS)

@Singleton
class FakeRemoteApi @Inject constructor(@ApplicationContext private val context: Context, private val flags: Flags) :
    RemoteApi {

    private val now = LocalDateTime.now()

    override suspend fun timeline(): RemoteLaunchesResponse {
        val originalResponse = Gson().fromJson(
            context.readFileFromAssets(flags.bootstrapResponseApiFileName),
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