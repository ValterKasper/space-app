package sk.kasper.domain.usecase.impl

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import sk.kasper.domain.model.Response
import sk.kasper.domain.usecase.GetOrbit
import sk.kasper.domain.utils.wrapNullableToResponse
import sk.kasper.entity.Orbit
import sk.kasper.repository.LaunchRepository
import javax.inject.Inject

internal class GetOrbitImpl @Inject constructor(private val launchRepository: LaunchRepository) : GetOrbit {

    override suspend operator fun invoke(launchId: String): Response<Orbit> = withContext(Dispatchers.IO) {
        wrapNullableToResponse { launchRepository.getOrbit(launchId) }
    }

}