package sk.kasper.domain.usecase.launchdetail

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import sk.kasper.domain.model.Response
import sk.kasper.domain.model.Rocket
import sk.kasper.domain.repository.RocketRepository
import sk.kasper.domain.utils.wrapToResponse
import javax.inject.Inject

class GetRocketForLaunch @Inject constructor(private val repository: RocketRepository){

    suspend fun getRocket(launchId: String): Response<Rocket> = withContext(Dispatchers.IO) {
        wrapToResponse { repository.getRocketForLaunch(launchId) }
    }

}