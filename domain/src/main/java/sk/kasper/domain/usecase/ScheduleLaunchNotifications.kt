package sk.kasper.domain.usecase

import sk.kasper.entity.Launch

fun interface ScheduleLaunchNotifications {
    operator fun invoke(launches: List<Launch>)
}