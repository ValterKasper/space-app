package sk.kasper.domain.usecase.impl

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import sk.kasper.domain.model.Response
import sk.kasper.domain.usecase.GetLaunchSite
import sk.kasper.domain.utils.wrapToResponse
import sk.kasper.entity.LaunchSite
import sk.kasper.repository.LaunchSiteRepository
import javax.inject.Inject

internal class GetLaunchSiteImpl @Inject constructor(private val launchSiteRepository: LaunchSiteRepository) :
    GetLaunchSite {

    override suspend operator fun invoke(launchId: String): Response<LaunchSite> =
        withContext(Dispatchers.IO) {
            wrapToResponse { launchSiteRepository.getLaunchSite(launchId) }
        }

}