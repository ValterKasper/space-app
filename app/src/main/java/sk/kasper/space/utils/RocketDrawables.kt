package sk.kasper.space.utils

import sk.kasper.domain.model.Rocket
import sk.kasper.space.R

fun rocketIdToDrawableRes(rocketId: Long?): Int {
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