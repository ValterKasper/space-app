package sk.kasper.repository

import sk.kasper.entity.Rocket2


interface RocketRepository2 {
    suspend fun getRocketForLaunch(launchId: String): Rocket2
}