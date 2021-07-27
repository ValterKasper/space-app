package sk.kasper.ui_timeline.ui

import sk.kasper.ui_common.tag.FilterDefinition
import sk.kasper.ui_common.tag.FilterRocket
import sk.kasper.ui_common.tag.FilterTag
import sk.kasper.ui_common.tag.LaunchFilterItem

val filterDefinition: FilterDefinition<LaunchFilterItem> = FilterDefinition(
    listOf(
        FilterRocket.FALCON_9,
        FilterTag.MARS,
        FilterTag.ISS,
        FilterTag.PROBE,
        FilterTag.SATELLITE,
    ), mapOf(
        FilterTag.ISS to listOf(FilterTag.MANNED, FilterRocket.SOYUZ),
        FilterTag.MARS to listOf(FilterTag.ROVER),
        FilterTag.SATELLITE to listOf(FilterTag.SECRET),
    )
)