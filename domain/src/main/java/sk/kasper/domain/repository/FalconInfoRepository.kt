package sk.kasper.domain.repository

import sk.kasper.entity.FalconCore

interface FalconInfoRepository {
    suspend fun getFalconCore(launchId: String): FalconCore?
}