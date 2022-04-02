package sk.kasper.space.repository

import sk.kasper.database.dao.LaunchDao
import sk.kasper.domain.repository.FalconInfoRepository
import sk.kasper.entity.FalconCore
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
// TODO D: should be repository module
class FalconInfoRepositoryImpl @Inject constructor(private val launchDao: LaunchDao) : FalconInfoRepository {

    override suspend fun getFalconCore(launchId: String): FalconCore? {
        return launchDao.getFalconCore(launchId).takeIf { it.isEmpty() }?.toFalconCore()
    }

}
