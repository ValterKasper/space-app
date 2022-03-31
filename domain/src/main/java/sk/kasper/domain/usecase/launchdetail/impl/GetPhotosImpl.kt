package sk.kasper.domain.usecase.launchdetail.impl

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import sk.kasper.domain.model.Photo
import sk.kasper.domain.model.Response
import sk.kasper.domain.repository.PhotoRepository
import sk.kasper.domain.usecase.launchdetail.GetPhotos
import sk.kasper.domain.utils.wrapToResponse
import javax.inject.Inject

internal class GetPhotosImpl @Inject constructor(private val photoRepository: PhotoRepository) : GetPhotos {

    override suspend operator fun invoke(launchId: String): Response<List<Photo>> = withContext(Dispatchers.IO) {
        wrapToResponse { photoRepository.getPhotosForLaunch(launchId) }
    }

}