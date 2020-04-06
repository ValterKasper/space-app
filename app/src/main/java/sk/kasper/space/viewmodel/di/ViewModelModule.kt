package sk.kasper.space.viewmodel.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.squareup.inject.assisted.dagger2.AssistedModule
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import sk.kasper.space.launchdetail.section.FalconInfoViewModel
import sk.kasper.space.launchdetail.section.GalleryViewModel
import sk.kasper.space.launchdetail.section.LaunchSiteViewModel
import sk.kasper.space.launchdetail.section.RocketSectionViewModel
import sk.kasper.space.viewmodel.ViewModelFactory


@AssistedModule
@Module(includes = [AssistedInject_ViewModelModule::class])
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(value = FalconInfoViewModel::class)
    abstract fun bindFalconInfoViewModel(viewModel: FalconInfoViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(value = RocketSectionViewModel::class)
    abstract fun bindRocketSectionViewModel(viewModel: RocketSectionViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(value = LaunchSiteViewModel::class)
    abstract fun bindLaunchSiteViewModel(viewModel: LaunchSiteViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(value = GalleryViewModel::class)
    abstract fun bindGalleryViewModel(viewModel: GalleryViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

}