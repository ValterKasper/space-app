package sk.kasper.base.init

import android.content.Context
import com.jakewharton.threetenabp.AndroidThreeTen
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

internal class JavaTimeInitializer @Inject constructor(
    @ApplicationContext private val context: Context
) : AppInitializer {

    override fun init() {
        AndroidThreeTen.init(context)
    }

}