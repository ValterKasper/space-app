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
import sk.kasper.space.launchdetail.gallery.GalleryAdapter
import sk.kasper.space.launchdetail.gallery.PhotoItem
import sk.kasper.space.launchdetail.gallery.PhotoPagerData
import sk.kasper.space.utils.SingleLiveEvent

class GalleryViewModel @AssistedInject constructor(
        @Assisted launchId: String,
        private val getPhotos: GetPhotos): SectionViewModel(), GalleryAdapter.PhotoClickListener {

    val galleryItems: MutableLiveData<List<Photo>> = MutableLiveData()
    val showPhotoPagerEvent: SingleLiveEvent<PhotoPagerData> = SingleLiveEvent()

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

    override fun onPhotoClicked(photoPosition: Int) {
        showPhotoPagerEvent.value = PhotoPagerData(
                photoPosition,
                galleryItems.value!!.map { PhotoItem(it.fullSizeUrl, it.sourceName, it.description) })
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(launchId: String): GalleryViewModel
    }

}