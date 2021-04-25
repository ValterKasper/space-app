package sk.kasper.space.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import sk.kasper.space.mainactivity.MainActivity
import sk.kasper.ui_common.di.ActivityScope

@Module
abstract class MockMainActivityModule {

    @ActivityScope
    @ContributesAndroidInjector(modules = [
        ViewModelModule::class,
        FragmentBuilder::class])
    abstract fun bindMainActivity(): MainActivity

}