package sk.kasper.remote.entity

data class RemoteLaunchSite(
    val id: Long,
    val siteName: String,
    val padName: String?,
    val infoURL: String?,
    val latitude: Double,
    val longitude: Double
)