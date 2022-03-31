package sk.kasper.domain.usecase.launchdetail.impl

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import sk.kasper.domain.model.FalconCore
import sk.kasper.domain.model.Response
import sk.kasper.domain.repository.FalconInfoRepository
import sk.kasper.domain.usecase.launchdetail.GetFalconCore
import sk.kasper.domain.utils.wrapNullableToResponse
import javax.inject.Inject

internal class GetFalconCoreImpl @Inject constructor(private val falconInfoRepository: FalconInfoRepository) :
    GetFalconCore {

    override suspend operator fun invoke(launchId: String): Response<FalconCore> = withContext(Dispatchers.IO) {
        wrapNullableToResponse { falconInfoRepository.getFalconCore(launchId) }
    }

}