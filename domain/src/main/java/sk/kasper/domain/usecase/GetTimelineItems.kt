package sk.kasper.domain.usecase

import sk.kasper.domain.model.FilterSpec
import sk.kasper.domain.model.Response
import sk.kasper.entity.Launch

interface GetTimelineItems {
    suspend operator fun invoke(filterSpec: FilterSpec = FilterSpec.EMPTY_FILTER): Response<List<Launch>>
}