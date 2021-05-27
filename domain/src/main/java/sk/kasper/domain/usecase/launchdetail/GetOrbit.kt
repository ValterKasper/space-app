package sk.kasper.domain.usecase.launchdetail

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import sk.kasper.domain.model.Orbit
import sk.kasper.domain.model.Response
import sk.kasper.domain.repository.LaunchRepository
import sk.kasper.domain.utils.wrapNullableToResponse
import javax.inject.Inject

class GetOrbit @Inject constructor(private val launchRepository: LaunchRepository) {
    suspend fun getOrbit(launchId: String): Response<Orbit> = withContext(Dispatchers.IO) {
        wrapNullableToResponse { launchRepository.getOrbit(launchId) }
    }
}