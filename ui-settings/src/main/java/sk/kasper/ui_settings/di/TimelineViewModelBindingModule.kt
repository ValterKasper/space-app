package sk.kasper.ui_settings.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import sk.kasper.ui_common.viewmodel.di.ViewModelKey
import sk.kasper.ui_settings.SettingsViewModel

@Module
interface SettingsViewModelsBindingModule {

    @Binds
    @IntoMap
    @ViewModelKey(SettingsViewModel::class)
    fun bindSettingsViewModel(viewModel: SettingsViewModel): ViewModel

}