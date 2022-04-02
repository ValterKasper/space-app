package sk.kasper.domain.model

import sk.kasper.entity.Rocket
import sk.kasper.entity.Tag

data class FilterSpec(val tagTypes: Set<Long> = emptySet(), val rockets: Set<Long> = emptySet()) {

    fun filterNotEmpty() = tagTypes.isNotEmpty() || rockets.isNotEmpty()

    companion object {
        val ALL_TAGS = setOf(Tag.ISS, Tag.MANNED, Tag.MARS, Tag.SECRET, Tag.SATELLITE, Tag.PROBE, Tag.ROVER, Tag.TEST_FLIGHT, Tag.CUBE_SAT)
        val ALL_ROCKETS = setOf(Rocket.FALCON_9, Rocket.FALCON_HEAVY, Rocket.SOYUZ, Rocket.ARIANE_5, Rocket.ATLAS_5, Rocket.DELTA_4_HEAVY)
        val EMPTY_FILTER = FilterSpec(emptySet(), emptySet())
    }

}