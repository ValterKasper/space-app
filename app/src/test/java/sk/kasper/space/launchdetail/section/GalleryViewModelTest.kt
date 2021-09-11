package sk.kasper.space.launchdetail.section

/*
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
*/
