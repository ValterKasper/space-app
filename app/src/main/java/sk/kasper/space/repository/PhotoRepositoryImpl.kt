package sk.kasper.space.repository

import sk.kasper.domain.model.Photo
import sk.kasper.domain.repository.PhotoRepository
import sk.kasper.space.database.PhotoDao
import javax.inject.Inject

class PhotoRepositoryImpl @Inject constructor(private val photoDao: PhotoDao): PhotoRepository {

    override suspend fun getPhotosForLaunch(launchId: String): List<Photo> {
        return photoDao.loadLaunchPhotos(launchId).map { it.toPhoto() }
    }

}