package sk.kasper.domain.usecase.impl

import sk.kasper.domain.model.ErrorResponse
import sk.kasper.domain.model.Response
import sk.kasper.domain.model.SuccessResponse
import sk.kasper.domain.usecase.RefreshTimelineItems
import sk.kasper.repository.SyncLaunchesRepository
import javax.inject.Inject

internal class RefreshTimelineItemsImpl @Inject constructor(private val syncLaunchesRepository: SyncLaunchesRepository) :
    RefreshTimelineItems {

    override suspend operator fun invoke(): Response<Unit> {
        return if (syncLaunchesRepository.doSync(force = true)) {
            SuccessResponse(Unit)
        } else {
            ErrorResponse()
        }
    }

}