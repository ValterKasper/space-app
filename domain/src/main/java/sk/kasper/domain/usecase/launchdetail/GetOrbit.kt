package sk.kasper.domain.usecase.launchdetail

import sk.kasper.domain.model.Orbit
import sk.kasper.domain.model.Response

interface GetOrbit {
    suspend operator fun invoke(launchId: String): Response<Orbit>
}