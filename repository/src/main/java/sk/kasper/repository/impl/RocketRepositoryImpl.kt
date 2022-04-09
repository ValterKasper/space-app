package sk.kasper.repository.impl

import sk.kasper.database.dao.RocketDao
import sk.kasper.entity.Rocket
import sk.kasper.repository.RocketRepository
import sk.kasper.repository.mapping.toRocket
import javax.inject.Inject

internal class RocketRepositoryImpl @Inject constructor(private val rocketDao: RocketDao) : RocketRepository {

    override suspend fun getRocketForLaunch(launchId: String): Rocket {
        return rocketDao.loadRocketByLaunchId(launchId).toRocket()
    }

}