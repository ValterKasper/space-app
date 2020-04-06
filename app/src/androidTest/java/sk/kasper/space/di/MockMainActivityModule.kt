package sk.kasper.space.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import sk.kasper.space.mainactivity.MainActivity
import sk.kasper.space.viewmodel.di.ViewModelModule

@Module
abstract class MockMainActivityModule {

    @ActivityScope
    @ContributesAndroidInjector(modules = [
        ViewModelModule::class,
        FragmentBuilder::class])
    abstract fun bindMainActivity(): MainActivity

}