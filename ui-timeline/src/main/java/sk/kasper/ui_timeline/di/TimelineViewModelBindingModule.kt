package sk.kasper.ui_timeline.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import sk.kasper.ui_common.viewmodel.di.ViewModelKey
import sk.kasper.ui_timeline.TimelineViewModel

@Module
interface TimelineViewModelsBindingModule {

    @Binds
    @IntoMap
    @ViewModelKey(TimelineViewModel::class)
    fun bindTimelineViewModel(viewModel: TimelineViewModel): ViewModel

}