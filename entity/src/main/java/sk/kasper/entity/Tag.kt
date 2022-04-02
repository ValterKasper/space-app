package sk.kasper.entity

data class Tag (
        val launchId: String,
        val type: Long) {

    companion object {
        const val ISS = 0L
        const val MANNED = 1L
        const val SATELLITE = 2L
        const val TEST_FLIGHT = 3L
        const val SECRET = 4L
        const val CUBE_SAT = 5L
        const val ROVER = 6L
        const val MARS = 7L
        const val PROBE = 8L
    }

}

