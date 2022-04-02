package sk.kasper.entity

data class Rocket(
        val id: Long,
        val rocketName: String,
        val height: Float,
        val diameter: Float,
        val mass: Float,
        val payloadLeo: Float,
        val payloadGto: Float,
        val thrust: Float,
        val stages: Int) {

    companion object {
        const val SOYUZ = 36L
        const val FALCON_9 = 80L
        const val FALCON_HEAVY = 58L
        const val ARIANE_5 = 27L
        const val ATLAS_5 = 26L
        const val DELTA_4_HEAVY = 21L
        const val ELECTRON = 148L
        const val LONG_MARCH_2C = 75L
        const val LONG_MARCH_3B = 22L
        const val LONG_MARCH_4B = 16L
        const val H_IIA = 23L
        const val H_IIB = 11L
        const val GSLV_MK_II = 60L
        const val GSLV_MK_III = 85L
    }

}