package sk.kasper.space.mainactivity

import android.os.Bundle
import androidx.annotation.VisibleForTesting
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import sk.kasper.space.R
import sk.kasper.ui_common.utils.backpress.BackPressManager
import javax.inject.Inject


class MainActivity : AppCompatActivity(), HasAndroidInjector {

    @Inject
    lateinit var backPressManager: BackPressManager

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    @VisibleForTesting
    var isIdleListener: IdleListener? = null

    interface IdleListener {
        fun onIdleStatusChanged(isIdle: Boolean)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // request to be laid out fullscreen
        WindowCompat.setDecorFitsSystemWindows(window, false)
    }

    override fun androidInjector(): AndroidInjector<Any> = dispatchingAndroidInjector

    fun setIdle(idle: Boolean) {
        isIdleListener?.onIdleStatusChanged(idle)
    }

    override fun onBackPressed() {
        if (!backPressManager.onBackPress()) {
            super.onBackPressed()
        }
    }

}
