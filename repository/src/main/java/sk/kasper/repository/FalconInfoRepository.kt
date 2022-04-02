package sk.kasper.repository

import sk.kasper.entity.FalconCore

interface FalconInfoRepository {
    suspend fun getFalconCore(launchId: String): FalconCore?
}