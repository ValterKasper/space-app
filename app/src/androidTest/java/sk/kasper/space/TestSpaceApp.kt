package sk.kasper.space

import sk.kasper.space.di.AppModule
import sk.kasper.space.di.DaggerTestAppComponent

class TestSpaceApp : SpaceApp() {

    override fun createAppComponent(): AggregatorModule {
        return DaggerTestAppComponent.builder()
            .appModule(AppModule(this))
            .build()
    }

}