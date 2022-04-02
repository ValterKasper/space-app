package sk.kasper.repository

import sk.kasper.entity.Rocket

interface RocketRepository {

    suspend fun getRocketForLaunch(launchId: String): Rocket

}