package sk.kasper.repository.impl

import sk.kasper.database.dao.LaunchDao
import sk.kasper.entity.FalconCore
import sk.kasper.repository.FalconInfoRepository
import javax.inject.Inject

internal class FalconInfoRepositoryImpl @Inject constructor(private val launchDao: LaunchDao) : FalconInfoRepository {

    override suspend fun getFalconCore(launchId: String): FalconCore? {
        return launchDao.getFalconCore(launchId).takeIf { it.isEmpty() }?.toFalconCore()
    }

}
