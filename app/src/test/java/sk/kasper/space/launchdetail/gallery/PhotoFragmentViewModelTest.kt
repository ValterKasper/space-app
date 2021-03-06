package sk.kasper.space.launchdetail.gallery

import org.junit.Test
import org.junit.runner.RunWith
import sk.kasper.ui_launch.gallery.PhotoFragmentViewModel

@RunWith(MockitoJUnitRunner::class)
class PhotoFragmentViewModelTest {

    private lateinit var viewModel: PhotoFragmentViewModel

    @Test
    fun url() {
        viewModel = PhotoFragmentViewModel(PhotoItem("url"))
        assertThat("url", `is`(viewModel.url))
    }

    @Test
    fun source() {
        viewModel = PhotoFragmentViewModel(PhotoItem("url", sourceName = "source"))
        assertThat("source", `is`(viewModel.source))
        assertThat(true, `is`(viewModel.sourceVisible))
    }

    @Test
    fun description() {
        viewModel = PhotoFragmentViewModel(PhotoItem("url", description = "description"))
        assertThat("description", `is`(viewModel.description))
        assertThat(true, `is`(viewModel.descriptionVisible))
    }

    @Test
    fun noTexts() {
        viewModel = PhotoFragmentViewModel(PhotoItem("url"))
        assertThat(false, `is`(viewModel.textsVisible))
    }
}