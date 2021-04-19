package sk.kasper.domain.repository

import sk.kasper.domain.model.FalconCore

interface FalconInfoRepository {
    suspend fun getFalconCore(launchId: String): FalconCore?
}