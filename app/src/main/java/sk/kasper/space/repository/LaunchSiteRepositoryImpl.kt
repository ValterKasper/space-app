package sk.kasper.space.repository

import sk.kasper.database.dao.LaunchSiteDao
import sk.kasper.domain.repository.LaunchSiteRepository
import sk.kasper.entity.LaunchSite
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LaunchSiteRepositoryImpl @Inject constructor(private val launchSiteDao: LaunchSiteDao) : LaunchSiteRepository {

    override suspend fun getLaunchSite(launchId: String): LaunchSite {
        return launchSiteDao.getLaunchSiteByLaunchId(launchId).toLaunchSite()
    }

}