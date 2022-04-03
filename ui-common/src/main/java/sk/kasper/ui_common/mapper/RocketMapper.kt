package sk.kasper.ui_common.mapper

import sk.kasper.entity.Rocket
import sk.kasper.ui_common.R
import sk.kasper.ui_common.tag.FilterRocket
import javax.inject.Inject

class RocketMapper @Inject constructor() {

    fun toDrawableRes(rocketId: Long?): Int {
        return when (rocketId) {
            Rocket.SOYUZ -> R.drawable.soyuz
            Rocket.ARIANE_5 -> R.drawable.ariane_5
            Rocket.ATLAS_5 -> R.drawable.atlas_5
            Rocket.FALCON_9 -> R.drawable.falcon_9
            Rocket.FALCON_HEAVY -> R.drawable.falcon_heavy
            Rocket.DELTA_4_HEAVY -> R.drawable.delta_iv_heavy
            Rocket.ELECTRON -> R.drawable.electron
            Rocket.LONG_MARCH_2C, Rocket.LONG_MARCH_3B, Rocket.LONG_MARCH_4B -> R.drawable.long_march
            Rocket.H_IIA, Rocket.H_IIB -> R.drawable.h_iib // good enough image for now for booth rockets
            Rocket.GSLV_MK_II -> R.drawable.gslv_mk_ii
            Rocket.GSLV_MK_III -> R.drawable.gslv_mk_iii
            else -> 0
        }
    }

    fun toStringRes(rocketId: Long?): Int {
        return when (rocketId) {
            Rocket.ARIANE_5 -> R.string.rocket_ariane_5
            Rocket.FALCON_9 -> R.string.rocket_falcon_9
            Rocket.SOYUZ -> R.string.rocket_soyuz
            Rocket.DELTA_4_HEAVY -> R.string.rocket_delta_VI_heavy
            Rocket.FALCON_HEAVY -> R.string.rocket_falcon_heavy
            Rocket.ATLAS_5 -> R.string.rocket_atlas_V
            else -> 0
        }
    }

    fun toDomainRocket(rocket: FilterRocket): Long {
        return when (rocket) {
            FilterRocket.SOYUZ -> Rocket.SOYUZ
            FilterRocket.ARIANE_5 -> Rocket.ARIANE_5
            FilterRocket.FALCON_9 -> Rocket.FALCON_9
            FilterRocket.DELTA_4_HEAVY -> Rocket.DELTA_4_HEAVY
            FilterRocket.FALCON_HEAVY -> Rocket.FALCON_HEAVY
            FilterRocket.ATLAS_5 -> Rocket.ATLAS_5
        }
    }

}