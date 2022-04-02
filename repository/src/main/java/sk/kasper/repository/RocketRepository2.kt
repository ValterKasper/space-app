package sk.kasper.repository

import sk.kasper.entity.Rocket2
import javax.inject.Inject

class RocketRepository2 @Inject constructor() {

    suspend fun getRocketForLaunch(launchId: String): Rocket2 {
        return Rocket2(2)
    }

}