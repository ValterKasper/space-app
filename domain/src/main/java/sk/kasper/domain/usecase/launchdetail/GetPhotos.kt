package sk.kasper.domain.usecase.launchdetail

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import sk.kasper.domain.model.Photo
import sk.kasper.domain.model.Response
import sk.kasper.domain.repository.PhotoRepository
import sk.kasper.domain.utils.wrapToResponse
import javax.inject.Inject

class GetPhotos @Inject constructor(private val photoRepository: PhotoRepository) {

    suspend fun getPhotos(launchId: String): Response<List<Photo>> = withContext(Dispatchers.IO) {
        wrapToResponse { photoRepository.getPhotosForLaunch(launchId) }
    }

}