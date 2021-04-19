package sk.kasper.space.repository

import sk.kasper.domain.model.FalconCore
import sk.kasper.domain.repository.FalconInfoRepository
import sk.kasper.space.database.LaunchDao
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FalconInfoRepositoryImpl @Inject constructor(private val launchDao: LaunchDao) : FalconInfoRepository {

    override suspend fun getFalconCore(launchId: String): FalconCore? {
        return launchDao.getFalconCore(launchId).takeIf { it.isEmpty() }?.toFalconCore()
    }

}
