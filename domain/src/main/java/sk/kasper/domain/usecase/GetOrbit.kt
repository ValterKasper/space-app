package sk.kasper.domain.usecase

import sk.kasper.domain.model.Response
import sk.kasper.entity.Orbit

fun interface GetOrbit {
    suspend operator fun invoke(launchId: String): Response<Orbit>
}