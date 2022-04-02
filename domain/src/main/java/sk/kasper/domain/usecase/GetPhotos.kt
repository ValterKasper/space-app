package sk.kasper.domain.usecase

import sk.kasper.domain.model.Response
import sk.kasper.entity.Photo

fun interface GetPhotos {
    suspend operator fun invoke(launchId: String): Response<List<Photo>>
}