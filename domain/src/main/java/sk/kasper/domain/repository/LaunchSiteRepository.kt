package sk.kasper.domain.repository

import sk.kasper.domain.model.LaunchSite

interface LaunchSiteRepository {

    suspend fun getLaunchSite(launchId: String): LaunchSite

}