package sk.kasper.space.launchdetail.section

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import kotlinx.coroutines.launch
import sk.kasper.domain.model.Photo
import sk.kasper.domain.model.SuccessResponse
import sk.kasper.domain.usecase.launchdetail.GetPhotos
import sk.kasper.space.BR
import sk.kasper.space.R

class GalleryViewModel @AssistedInject constructor(
        @Assisted launchId: Long,
        private val getPhotos: GetPhotos): SectionViewModel() {

    val galleryItems: MutableLiveData<List<Photo>> = MutableLiveData()

    init {
        title = R.string.gallery

        viewModelScope.launch {
            getPhotos.getPhotos(launchId).also {
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

    @AssistedInject.Factory
    interface Factory {
        fun create(launchId: Long): GalleryViewModel
    }

}