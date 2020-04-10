package sk.kasper.space.launchdetail.section

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import sk.kasper.domain.model.ErrorResponse
import sk.kasper.domain.model.Photo
import sk.kasper.domain.model.Response
import sk.kasper.domain.model.SuccessResponse
import sk.kasper.domain.usecase.launchdetail.GetPhotos
import sk.kasper.space.utils.CoroutinesMainDispatcherRule

@RunWith(MockitoJUnitRunner::class)
class GalleryViewModelTest {

    @Mock
    private lateinit var getPhotos: GetPhotos

    @get:Rule
    @ExperimentalCoroutinesApi
    var coroutinesMainDispatcherRule = CoroutinesMainDispatcherRule()

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun setLaunchId_loadGalleryItems_success_showItems() = runBlocking {
        val galleryItems = listOf(
                Photo("url1"),
                Photo("url2"))

        verifySetLaunchId(SuccessResponse(galleryItems), true) {
            assertEquals(galleryItems, it)
        }
    }

    @Test
    fun setLaunchId_loadGalleryItems_noItems_invisible() = runBlocking {
        verifySetLaunchId(SuccessResponse(emptyList()), false) {
            assertNull(it)
        }
    }

    @Test
    fun setLaunchId_loadGalleryItems_error_invisible() = runBlocking {
        verifySetLaunchId(ErrorResponse("error"), false) {
            assertNull(it)
        }
    }

    private suspend fun verifySetLaunchId(response: Response<List<Photo>>, expectedVisibility: Boolean, testObserverBlock: (List<Photo>?) -> Unit) {
        whenever(getPhotos.getPhotos(42L)).thenReturn(response)

        val galleryViewModel = GalleryViewModel(42, getPhotos)

        assertThat(galleryViewModel.visible, `is`(expectedVisibility))
        testObserverBlock(galleryViewModel.galleryItems.value)
    }

}