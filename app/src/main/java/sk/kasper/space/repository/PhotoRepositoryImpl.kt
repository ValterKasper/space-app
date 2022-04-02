package sk.kasper.space.repository

import sk.kasper.database.dao.PhotoDao
import sk.kasper.domain.repository.PhotoRepository
import sk.kasper.entity.Photo
import javax.inject.Inject

class PhotoRepositoryImpl @Inject constructor(private val photoDao: PhotoDao): PhotoRepository {

    override suspend fun getPhotosForLaunch(launchId: String): List<Photo> {
        return photoDao.loadLaunchPhotos(launchId).map { it.toPhoto() }
    }

}