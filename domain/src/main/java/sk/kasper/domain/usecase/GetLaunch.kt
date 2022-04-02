package sk.kasper.domain.usecase

import sk.kasper.entity.Launch

fun interface GetLaunch {
    suspend operator fun invoke(launchId: String): Launch
}