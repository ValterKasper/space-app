package sk.kasper.domain.usecase.launchdetail

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import sk.kasper.domain.model.Orbit
import sk.kasper.domain.repository.LaunchRepository
import javax.inject.Inject

class GetOrbit @Inject constructor(private val launchRepository: LaunchRepository) {
    suspend fun getOrbit(launchId: Long): Orbit = withContext(Dispatchers.IO) {
        launchRepository.getOrbit(launchId)
    }
}