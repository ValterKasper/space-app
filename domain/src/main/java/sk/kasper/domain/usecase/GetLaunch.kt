package sk.kasper.domain.usecase

import sk.kasper.domain.model.Launch

fun interface GetLaunch {
    suspend operator fun invoke(launchId: String): Launch
}