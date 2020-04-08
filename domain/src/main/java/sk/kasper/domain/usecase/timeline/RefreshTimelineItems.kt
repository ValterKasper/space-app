package sk.kasper.domain.usecase.timeline

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import sk.kasper.domain.model.Response
import sk.kasper.domain.model.SyncLaunches
import sk.kasper.domain.utils.wrapToResponse
import javax.inject.Inject

open class RefreshTimelineItems @Inject constructor(private val syncLaunches: SyncLaunches) {

    open suspend fun refresh(): Response<Boolean> = withContext(Dispatchers.IO) {
        wrapToResponse { syncLaunches.doSync(force = true) }
    }

}