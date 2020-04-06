package sk.kasper.domain.usecase.timeline

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import sk.kasper.domain.model.FilterSpec
import sk.kasper.domain.model.Launch
import sk.kasper.domain.model.SyncLaunches
import sk.kasper.domain.repository.LaunchRepository
import javax.inject.Inject

open class GetTimelineItems @Inject constructor(
        private val launchRepository: LaunchRepository,
        private val syncLaunches: SyncLaunches) {

    open suspend fun getTimelineItems(filterSpec: FilterSpec = FilterSpec.EMPTY_FILTER): List<Launch> {
        return withContext(Dispatchers.IO) {
            syncLaunches.doSync(force = false)
            return@withContext filterLaunches(launchRepository.getLaunches(), filterSpec)
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