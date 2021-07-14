package sk.kasper.space.utils

import android.content.Context
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import dagger.hilt.android.qualifiers.ApplicationContext
import sk.kasper.ui_launch.usecase.GoogleApiHelper
import javax.inject.Inject

class GoogleApiHelperImpl @Inject constructor(@ApplicationContext private val context: Context) :
    GoogleApiHelper {

    override fun isGoogleApiAvailable(): Boolean {
        return ConnectionResult.SUCCESS == GoogleApiAvailability.getInstance()
            .isGooglePlayServicesAvailable(context)
    }

}