package sk.kasper.domain.usecase.impl

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import sk.kasper.domain.model.Response
import sk.kasper.domain.usecase.GetRocketForLaunch
import sk.kasper.domain.utils.wrapToResponse
import sk.kasper.entity.Rocket
import sk.kasper.repository.RocketRepository
import javax.inject.Inject

internal class GetRocketForLaunchImpl @Inject constructor(private val repository: RocketRepository) :
    GetRocketForLaunch {

    override suspend operator fun invoke(launchId: String): Response<Rocket> = withContext(Dispatchers.IO) {
        wrapToResponse { repository.getRocketForLaunch(launchId) }
    }

}