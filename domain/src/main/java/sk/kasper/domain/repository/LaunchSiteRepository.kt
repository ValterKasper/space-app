package sk.kasper.domain.repository

import sk.kasper.entity.LaunchSite

interface LaunchSiteRepository {

    suspend fun getLaunchSite(launchId: String): LaunchSite

}