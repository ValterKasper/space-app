package sk.kasper.domain.usecase.impl

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import sk.kasper.domain.model.Launch
import sk.kasper.domain.repository.LaunchRepository
import sk.kasper.domain.usecase.GetLaunch
import javax.inject.Inject

internal class GetLaunchImpl @Inject constructor(private val launchRepository: LaunchRepository) : GetLaunch {

    override suspend operator fun invoke(launchId: String): Launch {
        return withContext(Dispatchers.IO) {
            launchRepository.getLaunch(launchId)
        }
    }

}