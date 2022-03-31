package sk.kasper.domain.usecase.launchdetail

import sk.kasper.domain.model.Launch

fun interface GetLaunch {
    suspend operator fun invoke(launchId: String): Launch
}