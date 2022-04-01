package sk.kasper.domain.usecase.impl

import sk.kasper.domain.model.*
import sk.kasper.domain.repository.LaunchRepository
import sk.kasper.domain.usecase.GetTimelineItems
import javax.inject.Inject

internal class GetTimelineItemsImpl @Inject constructor(
    private val launchRepository: LaunchRepository,
    private val syncLaunches: SyncLaunches,
) : GetTimelineItems {

    override suspend operator fun invoke(filterSpec: FilterSpec): Response<List<Launch>> {
        return if (syncLaunches.doSync(force = false)) {
            SuccessResponse(
                filterLaunches(
                    launchRepository.getLaunches(),
                    filterSpec
                )
            )
        } else {
            ErrorResponse()
        }
    }

    private fun filterLaunches(it: List<Launch>, filterSpec: FilterSpec) =
        it.filter {
            checkLaunch(it, filterSpec)
        }

    private fun checkLaunch(it: Launch, filterSpec: FilterSpec) =
        if (filterSpec.filterNotEmpty())
                it.tags.map { it.type }.any { filterSpec.tagTypes.contains(it) } or filterSpec.rockets.contains(it.rocketId)
            else
                true

}