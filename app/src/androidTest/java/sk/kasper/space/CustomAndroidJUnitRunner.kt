package sk.kasper.space

import android.app.Application
import android.content.Context
import android.preference.PreferenceManager
import androidx.test.runner.AndroidJUnitRunner
import com.jakewharton.threetenabp.AndroidThreeTen
import dagger.hilt.android.testing.HiltTestApplication

class CustomAndroidJUnitRunner : AndroidJUnitRunner() {

    @Throws(InstantiationException::class, IllegalAccessException::class, ClassNotFoundException::class)
    override fun newApplication(cl: ClassLoader, className: String, context: Context): Application {
        // TODO D: find nicer way how prepare tests
        AndroidThreeTen.init(context)
        PreferenceManager.getDefaultSharedPreferences(context).edit()
            .clear()
            .commit()
        return super.newApplication(cl, HiltTestApplication::class.java.name, context)
    }

}
