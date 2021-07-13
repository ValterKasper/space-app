package sk.kasper.ui_settings.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoMap
import sk.kasper.ui_common.viewmodel.di.ViewModelKey
import sk.kasper.ui_settings.SettingsViewModel

@InstallIn(SingletonComponent::class)
@Module
interface SettingsViewModelsBindingModule {

    @Binds
    @IntoMap
    @ViewModelKey(SettingsViewModel::class)
    fun bindSettingsViewModel(viewModel: SettingsViewModel): ViewModel

}