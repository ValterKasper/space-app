package sk.kasper.domain.usecase

import sk.kasper.domain.model.Response
import sk.kasper.entity.LaunchSite

interface GetLaunchSite {
    suspend operator fun invoke(launchId: String): Response<LaunchSite>
}