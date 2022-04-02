package sk.kasper.repository.impl

import sk.kasper.database.dao.PhotoDao
import sk.kasper.entity.Photo
import sk.kasper.repository.PhotoRepository
import javax.inject.Inject

internal class PhotoRepositoryImpl @Inject constructor(private val photoDao: PhotoDao) : PhotoRepository {

    override suspend fun getPhotosForLaunch(launchId: String): List<Photo> {
        return photoDao.loadLaunchPhotos(launchId).map { it.toPhoto() }
    }

}