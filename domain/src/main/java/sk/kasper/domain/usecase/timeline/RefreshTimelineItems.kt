package sk.kasper.domain.usecase.timeline

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import sk.kasper.domain.model.SyncLaunches
import javax.inject.Inject

open class RefreshTimelineItems @Inject constructor(private val syncLaunches: SyncLaunches) {

    // todo prepis tiez ako response
    open suspend fun refresh(): Boolean = withContext(Dispatchers.IO) {
        syncLaunches.doSync(force = true)
    }

}