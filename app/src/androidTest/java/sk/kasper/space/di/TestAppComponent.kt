package sk.kasper.space.di

import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import sk.kasper.space.work.WorkerBindingModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
        AndroidSupportInjectionModule::class,
        MockMainActivityModule::class,
        AssistedInjectModule::class,
        WorkerBindingModule::class,
        AppModule::class,
        MockDatabaseModule::class,
        MockRemoteApiModule::class
])
interface TestAppComponent: AppComponent