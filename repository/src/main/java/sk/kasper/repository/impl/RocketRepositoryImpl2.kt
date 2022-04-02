package sk.kasper.repository.impl

import sk.kasper.entity.Rocket2
import sk.kasper.repository.RocketRepository2
import javax.inject.Inject

internal class RocketRepositoryImpl2 @Inject constructor() : RocketRepository2 {

    override suspend fun getRocketForLaunch(launchId: String): Rocket2 {
        return Rocket2(2)
    }

}