package sk.kasper.space.api.entity

const val RESPONSE_CODE_OK = 0
const val RESPONSE_CODE_BAD_API_KEY = 5

data class RemoteLaunchesResponse(
        val responseCode: Int,
        val launches: List<RemoteLaunch>?,
        val launchSites: List<RemoteLaunchSite>?,
        val rockets: List<RemoteRocket>?,
        val photos: List<RemotePhoto>?)