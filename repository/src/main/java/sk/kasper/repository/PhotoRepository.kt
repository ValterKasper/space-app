package sk.kasper.repository

import sk.kasper.entity.Photo

interface PhotoRepository {

    suspend fun getPhotosForLaunch(launchId: String): List<Photo>

}