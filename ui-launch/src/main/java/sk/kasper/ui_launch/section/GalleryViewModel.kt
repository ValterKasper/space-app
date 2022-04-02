package sk.kasper.ui_launch.section

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import sk.kasper.domain.model.SuccessResponse
import sk.kasper.domain.usecase.GetPhotos
import sk.kasper.entity.Photo
import sk.kasper.ui_common.viewmodel.ReducerViewModel
import sk.kasper.ui_launch.R
import sk.kasper.ui_launch.gallery.PhotoItem
import sk.kasper.ui_launch.gallery.PhotoPagerData
import javax.inject.Inject

data class GalleryState(
    val title: Int = R.string.gallery,
    var visible: Boolean = true,
    val galleryItems: List<Photo> = emptyList()
)

sealed class GallerySideEffect
data class ShowPhotoPager(val photoPagerData: PhotoPagerData) : GallerySideEffect()

@HiltViewModel
class GalleryViewModel @Inject constructor(
    private val handle: SavedStateHandle,
    private val getPhotos: GetPhotos,
) : ReducerViewModel<GalleryState, GallerySideEffect>(GalleryState()) {

    init {
        initAction()
    }

    private fun initAction() = action {
        getPhotos(handle.get("launchId")!!).also {
            when (it) {
                is SuccessResponse -> reduce {
                    copy(
                        visible = it.data.isNotEmpty(),
                        galleryItems = it.data
                    )
                }
                else -> reduce {
                    copy(visible = false)
                }
            }
        }
    }

    fun onPhotoClicked(photo: Photo) = action {
        val oldState = snapshot()

        emitSideEffect(
            ShowPhotoPager(
                PhotoPagerData(oldState.galleryItems.indexOf(photo),
                    oldState.galleryItems.map {
                        PhotoItem(
                            it.fullSizeUrl,
                            it.sourceName,
                            it.description
                        )
                    })
            )
        )
    }

}