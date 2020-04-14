package sk.kasper.space.mainactivity

import android.os.Bundle
import androidx.annotation.VisibleForTesting
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import sk.kasper.space.FragmentTags
import sk.kasper.space.R
import sk.kasper.space.analytics.Analytics
import sk.kasper.space.launchdetail.LaunchFragment
import sk.kasper.space.playground.PlaygroundFragment
import sk.kasper.space.settings.SettingsFragment
import sk.kasper.space.timeline.TimelineFragment
import sk.kasper.space.utils.backpress.BackPressManager
import javax.inject.Inject


const val EXTRA_LAUNCH_ID = "extra-launch-id"
const val EXTRA_LAUNCH_ID_NOT_SELECTED = -1L

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

        if (savedInstanceState == null) {
            val launchId = intent.getLongExtra(EXTRA_LAUNCH_ID, EXTRA_LAUNCH_ID_NOT_SELECTED)

            if (launchId != EXTRA_LAUNCH_ID_NOT_SELECTED) {
                Analytics.log(
                        Analytics.Event.LAUNCH_NOTIF_TAP,
                        mapOf(Analytics.Param.ITEM_ID to launchId.toString()))

                replaceFragment(
                        TimelineFragment.newInstance(),
                        FragmentTags.TIMELINE,
                        addToBackStack = false)

                showLaunch(launchId)
            } else {
                replaceFragment(
                        TimelineFragment.newInstance(),
                        FragmentTags.TIMELINE,
                        addToBackStack = false)
            }

        }
    }

    override fun androidInjector(): AndroidInjector<Any> = dispatchingAndroidInjector

    fun setIdle(idle: Boolean) {
        isIdleListener?.onIdleStatusChanged(idle)
    }

    // todo navigation libka
    fun showLaunch(id: Long) {
        replaceFragment(LaunchFragment.newInstance(id), FragmentTags.LAUNCH, false, true,
                R.anim.enter_left, R.anim.exit_left,  R.anim.enter_right, R.anim.exit_right)
    }

    fun showSettings() {
        replaceFragment(SettingsFragment(), FragmentTags.SETTINGS, false, true,
                R.anim.enter_left, R.anim.exit_left,  R.anim.enter_right, R.anim.exit_right)
    }

    fun showPlayground() {
        replaceFragment(PlaygroundFragment(), FragmentTags.PLAYGROUND, false, true,
                R.anim.enter_left, R.anim.exit_left,  R.anim.enter_right, R.anim.exit_right)
    }

    private fun replaceFragment(fragment: Fragment, tag: String? = null, clearBackStack: Boolean = false, addToBackStack: Boolean = true,
                                enterAnimation: Int = 0, exitAnimation: Int = 0, popEnterAnimation: Int = 0, popExitAnimation: Int = 0) {
        if (clearBackStack) {
            supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }

        val transaction = supportFragmentManager.beginTransaction()

        transaction.setCustomAnimations(enterAnimation, exitAnimation, popEnterAnimation, popExitAnimation)
        transaction.replace(R.id.fragment_container, fragment)

        if (addToBackStack) {
            transaction.addToBackStack(tag)
        }

        transaction.commit()
    }

    override fun onBackPressed() {
        if (!backPressManager.onBackPress()) {
            super.onBackPressed()
        }
    }

}
