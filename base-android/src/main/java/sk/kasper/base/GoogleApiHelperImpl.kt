package sk.kasper.base

import android.content.Context
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

internal class GoogleApiHelperImpl @Inject constructor(@ApplicationContext private val context: Context) :
    GoogleApiHelper {

    override fun isGoogleApiAvailable(): Boolean {
        return ConnectionResult.SUCCESS == GoogleApiAvailability.getInstance()
            .isGooglePlayServicesAvailable(context)
    }

}