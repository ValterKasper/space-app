package sk.kasper.domain.usecase.impl

import sk.kasper.domain.model.ErrorResponse
import sk.kasper.domain.model.Response
import sk.kasper.domain.model.SuccessResponse
import sk.kasper.domain.model.SyncLaunches
import sk.kasper.domain.usecase.RefreshTimelineItems
import javax.inject.Inject

internal class RefreshTimelineItemsImpl @Inject constructor(private val syncLaunches: SyncLaunches) :
    RefreshTimelineItems {

    override suspend operator fun invoke(): Response<Unit> {
        return if (syncLaunches.doSync(force = true)) {
            SuccessResponse(Unit)
        } else {
            ErrorResponse()
        }
    }

}