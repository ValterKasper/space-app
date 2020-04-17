package sk.kasper.space.viewmodel.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import sk.kasper.space.playground.PlaygroundViewModel
import sk.kasper.space.viewmodel.ViewModelFactory

@Module
interface ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(value = PlaygroundViewModel::class)
    abstract fun bindPlaygroundViewModel(viewModel: PlaygroundViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

}