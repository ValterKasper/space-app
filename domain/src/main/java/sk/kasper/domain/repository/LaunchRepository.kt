package sk.kasper.domain.repository

import sk.kasper.entity.Launch
import sk.kasper.entity.Orbit

interface LaunchRepository {
    suspend fun getLaunches(): List<Launch>
    fun getLaunch(id: String): Launch
    fun getOrbit(id: String): Orbit?
}