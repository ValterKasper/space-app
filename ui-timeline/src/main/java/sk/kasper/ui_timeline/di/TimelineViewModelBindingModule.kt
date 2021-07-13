package sk.kasper.ui_timeline.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoMap
import sk.kasper.ui_common.viewmodel.di.ViewModelKey
import sk.kasper.ui_timeline.TimelineViewModel

@InstallIn(SingletonComponent::class)
@Module
interface TimelineViewModelsBindingModule {

    @Binds
    @IntoMap
    @ViewModelKey(TimelineViewModel::class)
    fun bindTimelineViewModel(viewModel: TimelineViewModel): ViewModel

}