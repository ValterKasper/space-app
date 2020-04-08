package sk.kasper.space.viewmodel.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.squareup.inject.assisted.dagger2.AssistedModule
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import sk.kasper.space.playground.PlaygroundViewModel
import sk.kasper.space.viewmodel.ViewModelFactory


@AssistedModule
@Module(includes = [AssistedInject_ViewModelModule::class])
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(value = PlaygroundViewModel::class)
    abstract fun bindPlaygroundViewModel(viewModel: PlaygroundViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

}