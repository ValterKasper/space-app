package sk.kasper.repository

import sk.kasper.entity.LaunchSite

interface LaunchSiteRepository {

    suspend fun getLaunchSite(launchId: String): LaunchSite

}