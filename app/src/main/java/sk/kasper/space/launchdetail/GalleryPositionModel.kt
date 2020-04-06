package sk.kasper.space.launchdetail

import androidx.lifecycle.ViewModel

// Holds current photo position between gallery in LaunchFragment and photo in PhotoPagerFragment
class GalleryPositionModel : ViewModel() {
    var position = 0
}