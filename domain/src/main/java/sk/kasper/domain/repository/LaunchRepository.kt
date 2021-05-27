package sk.kasper.domain.repository

import sk.kasper.domain.model.Launch
import sk.kasper.domain.model.Orbit

interface LaunchRepository {
    suspend fun getLaunches(): List<Launch>
    fun getLaunch(id: String): Launch
    fun getOrbit(id: String): Orbit?
}