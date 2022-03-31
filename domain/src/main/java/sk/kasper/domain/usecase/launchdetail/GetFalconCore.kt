package sk.kasper.domain.usecase.launchdetail

import sk.kasper.domain.model.FalconCore
import sk.kasper.domain.model.Response

fun interface GetFalconCore {
    suspend operator fun invoke(launchId: String): Response<FalconCore>
}