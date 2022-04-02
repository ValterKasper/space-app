package sk.kasper.domain.usecase.impl

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import sk.kasper.domain.model.Response
import sk.kasper.domain.usecase.GetPhotos
import sk.kasper.domain.utils.wrapToResponse
import sk.kasper.entity.Photo
import sk.kasper.repository.PhotoRepository
import javax.inject.Inject

internal class GetPhotosImpl @Inject constructor(private val photoRepository: PhotoRepository) : GetPhotos {

    override suspend operator fun invoke(launchId: String): Response<List<Photo>> = withContext(Dispatchers.IO) {
        wrapToResponse { photoRepository.getPhotosForLaunch(launchId) }
    }

}