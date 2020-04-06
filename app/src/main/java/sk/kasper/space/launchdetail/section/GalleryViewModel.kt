package sk.kasper.space.launchdetail.section

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import sk.kasper.domain.model.Photo
import sk.kasper.domain.model.SuccessResponse
import sk.kasper.domain.usecase.launchdetail.GetPhotos
import sk.kasper.space.BR
import sk.kasper.space.R
import javax.inject.Inject

class GalleryViewModel @Inject constructor(
        private val getPhotos: GetPhotos): SectionViewModel() {

    val galleryItems: MutableLiveData<List<Photo>> = MutableLiveData()

    init {
        title = R.string.gallery
    }

    // todo assited
    var launchId: Long = 0L
        set(value) {
            if (field == 0L) {
                field = value

                viewModelScope.launch {
                    getPhotos.getPhotos(field).also {
                        when (it) {
                            is SuccessResponse -> {
                                visible = if (it.data.isNotEmpty()) {
                                    galleryItems.value = it.data
                                    true
                                } else {
                                    false
                                }
                                notifyPropertyChanged(BR.visible)
                            }
                            else -> {
                                visible = false
                                notifyPropertyChanged(BR.visible)
                            }
                        }
                    }
                }
            }
        }

}