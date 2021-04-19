package sk.kasper.space.repository

import org.threeten.bp.Duration
import org.threeten.bp.LocalDateTime
import sk.kasper.domain.model.Launch
import sk.kasper.domain.model.Orbit
import sk.kasper.domain.repository.LaunchRepository
import sk.kasper.space.BuildConfig
import sk.kasper.space.database.LaunchDao
import sk.kasper.space.utils.safeEnumValueOf
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class LaunchRepositoryImpl @Inject constructor(private val launchDao: LaunchDao) : LaunchRepository {

    companion object {
        val TOO_OLD_DURATION: Duration = Duration.ofHours(BuildConfig.TOO_OLD_LAUNCH_TO_BE_SHOWN_HOURS)
    }

    override suspend fun getLaunches(): List<Launch> {
        return launchDao
                .getLaunches()
                .map { it.toLaunch() }
                .filter { it.launchDateTime.isAfter(getCurrentDateTime().minus(TOO_OLD_DURATION)) }
    }

    override fun getLaunch(id: String): Launch {
        return launchDao.getLaunch(id).toLaunch()
    }

    override fun getOrbit(id: String): Orbit {
        return launchDao.getOrbit(id)?.let {
            safeEnumValueOf(it, Orbit.UNKNOWN)
        } ?: Orbit.UNKNOWN
    }

    protected open fun getCurrentDateTime(): LocalDateTime {
        return LocalDateTime.now()
    }

}

