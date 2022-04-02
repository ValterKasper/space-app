package sk.kasper.domain.usecase

import sk.kasper.domain.model.Response
import sk.kasper.entity.FalconCore

fun interface GetFalconCore {
    suspend operator fun invoke(launchId: String): Response<FalconCore>
}