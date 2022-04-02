package sk.kasper.space.repository

import sk.kasper.database.dao.RocketDao
import sk.kasper.domain.repository.RocketRepository
import sk.kasper.entity.Rocket
import javax.inject.Inject

class RocketRepositoryImpl @Inject constructor(private val rocketDao: RocketDao): RocketRepository {

    override suspend fun getRocketForLaunch(launchId: String): Rocket {
        return rocketDao.loadRocketByLaunchId(launchId).toRocket()
    }

}