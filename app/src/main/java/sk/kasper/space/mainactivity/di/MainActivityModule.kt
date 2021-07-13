package sk.kasper.space.mainactivity.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.hilt.InstallIn
import dagger.hilt.android.scopes.ActivityScoped
import dagger.hilt.components.SingletonComponent
import sk.kasper.space.mainactivity.MainActivity
import sk.kasper.ui_common.viewmodel.di.ViewModelModule


@InstallIn(SingletonComponent::class)
@Module
abstract class MainActivityModule {

    @ActivityScoped
    @ContributesAndroidInjector(
        modules = [
            ViewModelModule::class
        ]
    )
    abstract fun bindMainActivity(): MainActivity

}