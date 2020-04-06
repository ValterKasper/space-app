package sk.kasper.space.timeline.filter

import sk.kasper.domain.model.Rocket
import sk.kasper.space.R

open class RocketViewModel(rocketId: Long) {

    val label = when (rocketId) {
        Rocket.ARIANE_5 -> R.string.rocket_ariane_5
        Rocket.FALCON_9 -> R.string.rocket_falcon_9
        Rocket.SOYUZ -> R.string.rocket_soyuz
        Rocket.DELTA_4_HEAVY -> R.string.rocket_delta_VI_heavy
        Rocket.FALCON_HEAVY -> R.string.rocket_falcon_heavy
        Rocket.ATLAS_5 -> R.string.rocket_atlas_V
        else -> throw IllegalStateException("Unknown ID of rocket in filter: $rocketId")
    }

}