package sk.kasper.domain.usecase.launchdetail

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import sk.kasper.domain.model.Launch
import sk.kasper.domain.repository.LaunchRepository
import javax.inject.Inject

open class GetLaunch @Inject constructor(private val launchRepository: LaunchRepository){

    // todo co ked nastane chyba. Ked launchId neexistuje? Skus najprv testy
    open suspend fun getLaunch(launchId: Long): Launch {
        return withContext(Dispatchers.IO) {
            launchRepository.getLaunch(launchId)
        }
    }

}