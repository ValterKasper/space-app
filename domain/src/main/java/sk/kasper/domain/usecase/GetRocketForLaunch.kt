package sk.kasper.domain.usecase

import sk.kasper.domain.model.Response
import sk.kasper.entity.Rocket

interface GetRocketForLaunch {
    suspend operator fun invoke(launchId: String): Response<Rocket>
}