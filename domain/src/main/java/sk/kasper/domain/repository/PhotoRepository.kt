package sk.kasper.domain.repository

import sk.kasper.domain.model.Photo

// TODO D: make internal all repos
interface PhotoRepository {

    suspend fun getPhotosForLaunch(launchId: String): List<Photo>

}