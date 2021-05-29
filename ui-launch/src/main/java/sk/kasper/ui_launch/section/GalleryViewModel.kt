package sk.kasper.ui_launch.section

import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import sk.kasper.domain.model.Photo
import sk.kasper.domain.model.SuccessResponse
import sk.kasper.domain.usecase.launchdetail.GetPhotos
import sk.kasper.ui_common.viewmodel.ReducerViewModel
import sk.kasper.ui_launch.R
import sk.kasper.ui_launch.gallery.PhotoItem
import sk.kasper.ui_launch.gallery.PhotoPagerData

data class GalleryState(
    val title: Int = R.string.gallery,
    var visible: Boolean = true,
    val galleryItems: List<Photo> = emptyList()
)

sealed class GallerySideEffect
data class ShowPhotoPager(val photoPagerData: PhotoPagerData) : GallerySideEffect()

sealed class GalleryAction
data class OnPhotoClicked(val photo: Photo) : GalleryAction()
data class ShowGalleryItems(val galleryItems: List<Photo>) : GalleryAction()
object ShowError : GalleryAction()
object Init : GalleryAction()

class GalleryViewModel @AssistedInject constructor(
    @Assisted private val launchId: String,
    private val getPhotos: GetPhotos
) : ReducerViewModel<GalleryState, GalleryAction, GallerySideEffect>(GalleryState()) {

    init {
        submitAction(Init)
    }

    override fun mapActionToActionFlow(action: GalleryAction): Flow<GalleryAction> {
        return if (action is Init) {
            flow {
                getPhotos.getPhotos(launchId).also {
                    when (it) {
                        is SuccessResponse -> emit(ShowGalleryItems(it.data))
                        else -> emit(ShowError)
                    }
                }
            }
        } else {
            super.mapActionToActionFlow(action)
        }
    }

    override fun ScanScope.scan(action: GalleryAction, oldState: GalleryState): GalleryState {
        return when (action) {
            is OnPhotoClicked -> {
                emitSideEffect(
                    ShowPhotoPager(
                        PhotoPagerData(oldState.galleryItems.indexOf(action.photo),
                            oldState.galleryItems.map {
                                PhotoItem(
                                    it.fullSizeUrl,
                                    it.sourceName,
                                    it.description
                                )
                            })
                    )
                )
                oldState
            }
            is ShowError -> {
                oldState.copy(visible = false)
            }
            is ShowGalleryItems -> {
                oldState.copy(
                    visible = action.galleryItems.isNotEmpty(),
                    galleryItems = action.galleryItems
                )
            }
            else -> oldState
        }
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(launchId: String): GalleryViewModel
    }

}