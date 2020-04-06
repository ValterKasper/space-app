package sk.kasper.space.launchdetail.gallery

import sk.kasper.space.utils.ObservableViewModel

class PhotoFragmentViewModel(photoItem: PhotoItem): ObservableViewModel() {

    val url = photoItem.url
    val sourceVisible = !photoItem.sourceName.isNullOrEmpty()
    val descriptionVisible = !photoItem.description.isNullOrEmpty()
    val textsVisible = sourceVisible || descriptionVisible

    val source = photoItem.sourceName
    val description = photoItem.description

}