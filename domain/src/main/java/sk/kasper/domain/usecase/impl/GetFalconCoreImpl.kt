package sk.kasper.domain.usecase.impl

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import sk.kasper.domain.model.Response
import sk.kasper.domain.usecase.GetFalconCore
import sk.kasper.domain.utils.wrapNullableToResponse
import sk.kasper.entity.FalconCore
import sk.kasper.repository.FalconInfoRepository
import javax.inject.Inject

internal class GetFalconCoreImpl @Inject constructor(private val falconInfoRepository: FalconInfoRepository) :
    GetFalconCore {

    override suspend operator fun invoke(launchId: String): Response<FalconCore> = withContext(Dispatchers.IO) {
        wrapNullableToResponse { falconInfoRepository.getFalconCore(launchId) }
    }

}