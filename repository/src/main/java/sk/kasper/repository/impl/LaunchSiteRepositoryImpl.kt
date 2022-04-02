package sk.kasper.repository.impl

import sk.kasper.database.dao.LaunchSiteDao
import sk.kasper.entity.LaunchSite
import sk.kasper.repository.LaunchSiteRepository
import javax.inject.Inject

internal class LaunchSiteRepositoryImpl @Inject constructor(private val launchSiteDao: LaunchSiteDao) :
    LaunchSiteRepository {

    override suspend fun getLaunchSite(launchId: String): LaunchSite {
        return launchSiteDao.getLaunchSiteByLaunchId(launchId).toLaunchSite()
    }

}