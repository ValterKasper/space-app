package sk.kasper.ui_common.viewmodel.di

import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import sk.kasper.ui_common.viewmodel.ViewModelFactory

@InstallIn(SingletonComponent::class)
@Module
interface ViewModelModule {

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

}