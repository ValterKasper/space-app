package sk.kasper.space.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import sk.kasper.space.mainactivity.MainActivity

@Module
abstract class MockMainActivityModule {

    @ActivityScope
    @ContributesAndroidInjector(modules = [
        ViewModelModule::class,
        FragmentBuilder::class])
    abstract fun bindMainActivity(): MainActivity

}