package sk.kasper.domain.usecase

import sk.kasper.domain.model.Response

fun interface RefreshTimelineItems {
    suspend operator fun invoke(): Response<Unit>
}