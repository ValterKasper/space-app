package sk.kasper.domain.usecase.impl

import kotlinx.coroutines.flow.Flow
import sk.kasper.domain.usecase.ObserveLaunches
import sk.kasper.entity.Launch
import sk.kasper.repository.LaunchRepository
import javax.inject.Inject

internal class ObserveLaunchesImpl @Inject constructor(private val launchRepository: LaunchRepository) :
    ObserveLaunches {

    override suspend fun invoke(): Flow<List<Launch>> {
        return launchRepository.observeLaunches()
    }

}