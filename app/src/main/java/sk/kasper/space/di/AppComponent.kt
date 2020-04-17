package sk.kasper.space.di

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import sk.kasper.space.SpaceApp
import sk.kasper.space.api.RemoteApi
import sk.kasper.space.api.di.RemoteApiModule
import sk.kasper.space.database.di.DatabaseModule
import sk.kasper.space.mainactivity.di.MainActivityModule
import sk.kasper.space.work.WorkerBindingModule
import javax.inject.Singleton


@Singleton
@Component(modules = arrayOf(
        AndroidSupportInjectionModule::class,
        MainActivityModule::class,
        AppModule::class,
        AssistedInjectModule::class,
        WorkerBindingModule::class,
        RemoteApiModule::class,
        DatabaseModule::class))
interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder
        fun appModule(appModule: AppModule): Builder
        fun build(): AppComponent
    }

    fun inject(app: SpaceApp)
    fun remoteApi(): RemoteApi

}