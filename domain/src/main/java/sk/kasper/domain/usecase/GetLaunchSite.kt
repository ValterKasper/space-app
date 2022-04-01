package sk.kasper.domain.usecase

import sk.kasper.domain.model.LaunchSite
import sk.kasper.domain.model.Response

interface GetLaunchSite {
    suspend operator fun invoke(launchId: String): Response<LaunchSite>
}