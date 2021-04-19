package sk.kasper.space.launchdetail.section

import androidx.annotation.StringRes
import androidx.databinding.Bindable
import androidx.lifecycle.viewModelScope
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import kotlinx.coroutines.launch
import sk.kasper.domain.model.Orbit
import sk.kasper.domain.usecase.launchdetail.GetOrbit
import sk.kasper.space.R
import sk.kasper.space.utils.ObservableViewModel
import sk.kasper.space.view.OrbitToDraw
import timber.log.Timber

class OrbitViewModel @AssistedInject constructor(
        private val getOrbit: GetOrbit,
        @Assisted private val launchId: String): ObservableViewModel() {

    @Bindable
    var visible = false

    @Bindable
    var orbit = OrbitToDraw.LEO

    @Bindable
    @StringRes
    var orbitName = R.string.geostationary_orbit

    init {
        viewModelScope.launch {
            try {
                when (getOrbit.getOrbit(launchId)) {
                    Orbit.LEO -> {
                        visible = true
                        orbit = OrbitToDraw.LEO
                        orbitName = R.string.low_earth_orbit
                    }
                    Orbit.GEO, Orbit.GTO -> {
                        visible = true
                        orbit = OrbitToDraw.GEO
                        orbitName = R.string.geostationary_orbit
                    }
                    else -> visible = false
                }
                notifyChange()
            } catch (e: Exception) {
                Timber.e(e, "Unexpected GetOrbit.getOrbit error response.")
            }
        }
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(launchId: String): OrbitViewModel
    }

}