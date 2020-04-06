package sk.kasper.space.repository

import sk.kasper.domain.model.Rocket
import sk.kasper.domain.repository.RocketRepository
import sk.kasper.space.database.RocketDao
import javax.inject.Inject

class RocketRepositoryImpl @Inject constructor(private val rocketDao: RocketDao): RocketRepository {

    override suspend fun getRocketForLaunch(launchId: Long): Rocket {
        return rocketDao.loadRocketByLaunchId(launchId).toRocket()
    }

}