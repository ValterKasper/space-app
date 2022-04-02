package sk.kasper.ui_launch.section

import androidx.lifecycle.SavedStateHandle
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import sk.kasper.domain.model.ErrorResponse
import sk.kasper.domain.model.SuccessResponse
import sk.kasper.entity.Photo
import sk.kasper.ui_launch.gallery.PhotoItem
import sk.kasper.ui_launch.gallery.PhotoPagerData

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class GalleryViewModelTest {

    private companion object {
        private const val LAUNCH_ID = "100"
        private val PHOTO_1 = Photo(thumbnailUrl = "url 1")
        private val PHOTO_2 = Photo(thumbnailUrl = "url 2")
    }

    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when getting photos returns error response then section is not visible`() = runTest {
        val getPhotos = { id: String ->
            assertThat(id).isEqualTo(LAUNCH_ID)
            ErrorResponse()
        }

        val galleryViewModel =
            GalleryViewModel(SavedStateHandle(mapOf("launchId" to LAUNCH_ID)), getPhotos)

        advanceUntilIdle()

        assertThat(galleryViewModel.state.value).isEqualTo(GalleryState(visible = false))
    }

    @Test
    fun `when photos exist then show them`() = runTest {
        val getPhotos = { id: String ->
            assertThat(id).isEqualTo(LAUNCH_ID)
            SuccessResponse(listOf(PHOTO_1))
        }

        val galleryViewModel =
            GalleryViewModel(SavedStateHandle(mapOf("launchId" to LAUNCH_ID)), getPhotos)

        advanceUntilIdle()

        assertThat(galleryViewModel.state.value).isEqualTo(GalleryState(galleryItems = listOf(PHOTO_1)))
    }

    @Test
    fun `when photo is clicked then photo should be shown`() = runTest {
        val getPhotos = { id: String ->
            assertThat(id).isEqualTo(LAUNCH_ID)
            SuccessResponse(listOf(PHOTO_1, PHOTO_2))
        }

        val galleryViewModel =
            GalleryViewModel(SavedStateHandle(mapOf("launchId" to LAUNCH_ID)), getPhotos)

        launch {
            val first = galleryViewModel.sideEffects.first()

            assertThat(first).isEqualTo(
                ShowPhotoPager(
                    PhotoPagerData(
                        1, listOf(
                            PhotoItem(PHOTO_1.fullSizeUrl),
                            PhotoItem(PHOTO_2.fullSizeUrl)
                        )
                    )
                )
            )
        }

        galleryViewModel.onPhotoClicked(PHOTO_2)
    }
}