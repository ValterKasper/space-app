package sk.kasper.repository.impl

import org.threeten.bp.Duration
import org.threeten.bp.LocalDateTime
import sk.kasper.database.dao.LaunchDao
import sk.kasper.entity.Launch
import sk.kasper.entity.Orbit
import sk.kasper.repository.LaunchRepository
import sk.kasper.space.utils.enumValueOrNull
import javax.inject.Inject

internal open class LaunchRepositoryImpl @Inject constructor(private val launchDao: LaunchDao) : LaunchRepository {

    companion object {
        val TOO_OLD_DURATION: Duration = Duration.ofHours(24)
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

    override fun getOrbit(id: String): Orbit? {
        return launchDao.getOrbit(id)?.let {
            enumValueOrNull<Orbit>(it)
        }
    }

    protected open fun getCurrentDateTime(): LocalDateTime {
        return LocalDateTime.now()
    }

}

