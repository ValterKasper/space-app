package sk.kasper.space.di

import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(
        AndroidSupportInjectionModule::class,
        MockMainActivityModule::class,
        ServiceBuilder::class,
        AppModule::class,
        MockDatabaseModule::class,
        MockRemoteApiModule::class
))
interface TestAppComponent: AppComponent