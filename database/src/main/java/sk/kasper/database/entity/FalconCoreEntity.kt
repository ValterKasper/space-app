package sk.kasper.database.entity

data class FalconCoreEntity(val reused: Boolean?,
                            val block: Int?,
                            val flights: Int?,
                            val landingType: String?,
                            val landingVehicle: String?) {

    companion object {
        internal const val EMBEDDED_PREFIX = "falconCore_"
    }

    fun isEmpty(): Boolean {
        return reused == null || block == null || flights == null || landingType == null || landingVehicle == null
    }

}