package sk.kasper.ui_launch.gallery

import sk.kasper.space.launchdetail.gallery.PhotoItem
import sk.kasper.ui_common.utils.ObservableViewModel

class PhotoFragmentViewModel(photoItem: PhotoItem): ObservableViewModel() {

    val url = photoItem.url
    val sourceVisible = !photoItem.sourceName.isNullOrEmpty()
    val descriptionVisible = !photoItem.description.isNullOrEmpty()
    val textsVisible = sourceVisible || descriptionVisible

    val source = photoItem.sourceName
    val description = photoItem.description

}