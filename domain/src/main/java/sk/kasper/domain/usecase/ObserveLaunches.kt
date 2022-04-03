package sk.kasper.domain.usecase

import kotlinx.coroutines.flow.Flow
import sk.kasper.entity.Launch

fun interface ObserveLaunches {
    suspend operator fun invoke(): Flow<List<Launch>>
}