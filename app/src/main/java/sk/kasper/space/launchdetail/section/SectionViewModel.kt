package sk.kasper.space.launchdetail.section

import androidx.databinding.Bindable
import sk.kasper.ui_common.utils.ObservableViewModel

abstract class SectionViewModel : ObservableViewModel() {

    @get:Bindable
    var title: Int = 0

    @get:Bindable
    var visible: Boolean = true

}