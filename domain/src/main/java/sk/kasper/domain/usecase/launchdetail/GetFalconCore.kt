package sk.kasper.domain.usecase.launchdetail

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import sk.kasper.domain.model.FalconCore
import sk.kasper.domain.model.Response
import sk.kasper.domain.repository.FalconInfoRepository
import sk.kasper.domain.utils.wrapNullableToResponse
import javax.inject.Inject

class GetFalconCore @Inject constructor(private val falconInfoRepository: FalconInfoRepository) {

    suspend fun getFalconCore(launchId: Long): Response<FalconCore> = withContext(Dispatchers.IO) {
        wrapNullableToResponse { falconInfoRepository.getFalconCore(launchId) }
    }

}