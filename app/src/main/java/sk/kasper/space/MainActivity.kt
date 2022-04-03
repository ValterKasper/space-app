package sk.kasper.space

import android.os.Bundle
import androidx.annotation.VisibleForTesting
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import dagger.hilt.android.AndroidEntryPoint
import sk.kasper.ui_common.utils.backpress.BackPressManager
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var backPressManager: BackPressManager

    @VisibleForTesting
    var isIdleListener: IdleListener? = null

    interface IdleListener {
        fun onIdleStatusChanged(isIdle: Boolean)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // request to be laid out fullscreen
        WindowCompat.setDecorFitsSystemWindows(window, false)
    }

    fun setIdle(idle: Boolean) {
        isIdleListener?.onIdleStatusChanged(idle)
    }

    override fun onBackPressed() {
        if (!backPressManager.onBackPress()) {
            super.onBackPressed()
        }
    }

}
