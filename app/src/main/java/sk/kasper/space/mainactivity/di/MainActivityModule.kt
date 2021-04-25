package sk.kasper.space.mainactivity.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import sk.kasper.ui_common.di.ActivityScope
import sk.kasper.space.di.FragmentBuilder
import sk.kasper.space.mainactivity.MainActivity
import sk.kasper.ui_common.viewmodel.di.ViewModelModule


@Module
abstract class MainActivityModule {

    @ActivityScope
    @ContributesAndroidInjector(modules = [
        ViewModelModule::class,
        FragmentBuilder::class])
    abstract fun bindMainActivity(): MainActivity

}