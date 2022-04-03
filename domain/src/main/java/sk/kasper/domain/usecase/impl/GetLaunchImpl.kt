package sk.kasper.domain.usecase.impl

import sk.kasper.domain.usecase.GetLaunch
import sk.kasper.entity.Launch
import sk.kasper.repository.LaunchRepository
import javax.inject.Inject

internal class GetLaunchImpl @Inject constructor(private val launchRepository: LaunchRepository) : GetLaunch {

    override suspend operator fun invoke(launchId: String): Launch {
        return launchRepository.getLaunch(launchId)
    }

}