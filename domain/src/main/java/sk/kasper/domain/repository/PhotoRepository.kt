package sk.kasper.domain.repository

import sk.kasper.domain.model.Photo

interface PhotoRepository {

    suspend fun getPhotosForLaunch(launchId: Long): List<Photo>

}