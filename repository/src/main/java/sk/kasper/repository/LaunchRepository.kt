package sk.kasper.repository

import kotlinx.coroutines.flow.Flow
import sk.kasper.entity.Launch
import sk.kasper.entity.Orbit

interface LaunchRepository {
    suspend fun getLaunches(): List<Launch>
    fun observeLaunches(): Flow<List<Launch>>
    suspend fun getLaunch(id: String): Launch
    suspend fun getOrbit(id: String): Orbit?
}