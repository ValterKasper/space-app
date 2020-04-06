package sk.kasper.domain.usecase.launchdetail

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import sk.kasper.domain.model.LaunchSite
import sk.kasper.domain.model.Response
import sk.kasper.domain.repository.LaunchSiteRepository
import sk.kasper.domain.utils.wrapToResponse
import javax.inject.Inject

open class GetLaunchSite @Inject constructor(private val launchSiteRepository: LaunchSiteRepository){

    suspend fun getLaunchSite(launchId: Long): Response<LaunchSite>  = withContext(Dispatchers.IO) {
        wrapToResponse { launchSiteRepository.getLaunchSite(launchId) }
    }

}