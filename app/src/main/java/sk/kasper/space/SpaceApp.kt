package sk.kasper.space

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import sk.kasper.base.init.AppInitializers
import javax.inject.Inject

@HiltAndroidApp
open class SpaceApp : Application() {

    @Inject
    lateinit var appInitializers: AppInitializers

    override fun onCreate() {
        super.onCreate()
        appInitializers.init()
    }
}

