package sk.kasper.domain.repository

import sk.kasper.domain.model.Rocket

interface RocketRepository {

    suspend fun getRocketForLaunch(launchId: Long): Rocket

}