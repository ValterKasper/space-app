package sk.kasper.domain.usecase.timeline

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import sk.kasper.domain.model.ErrorResponse
import sk.kasper.domain.model.Response
import sk.kasper.domain.model.SuccessResponse
import sk.kasper.domain.model.SyncLaunches
import javax.inject.Inject

open class RefreshTimelineItems @Inject constructor(private val syncLaunches: SyncLaunches) {

    open suspend fun refresh(): Response<Unit> = withContext(Dispatchers.IO) {
        if (syncLaunches.doSync(force = true)) {
            SuccessResponse(Unit)
        } else {
            ErrorResponse()
        }
    }

}