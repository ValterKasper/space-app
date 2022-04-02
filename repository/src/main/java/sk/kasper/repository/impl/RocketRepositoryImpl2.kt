package sk.kasper.repository.impl

import sk.kasper.database.dao.CarDao
import sk.kasper.entity.Rocket2
import sk.kasper.repository.RocketRepository2
import javax.inject.Inject

// TODO D: presun celu db database modulu
internal class RocketRepositoryImpl2 @Inject constructor(val carDao: CarDao) : RocketRepository2 {

    override suspend fun getRocketForLaunch(launchId: String): Rocket2 {
        return Rocket2(2)
    }

}