package sk.kasper.space.mapper

import sk.kasper.entity.Rocket
import sk.kasper.ui_common.R
import sk.kasper.ui_common.rocket.MapFilterRocketToDomainRocket
import sk.kasper.ui_common.rocket.MapRocketToDrawableRes
import sk.kasper.ui_common.rocket.MapRocketToStringRes
import sk.kasper.ui_common.rocket.RocketMapper
import sk.kasper.ui_common.tag.FilterRocket
import javax.inject.Inject

class RocketMapperImpl @Inject constructor() : RocketMapper, MapFilterRocketToDomainRocket, MapRocketToDrawableRes,
    MapRocketToStringRes {

    override fun toDrawableRes(rocketId: Long?): Int {
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

    override fun toStringRes(rocketId: Long?): Int {
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

    override fun toDomainRocket(rocket: FilterRocket): Long {
        return when (rocket) {
            FilterRocket.SOYUZ -> Rocket.SOYUZ
            FilterRocket.ARIANE_5 -> Rocket.ARIANE_5
            FilterRocket.FALCON_9 -> Rocket.FALCON_9
            FilterRocket.DELTA_4_HEAVY -> Rocket.DELTA_4_HEAVY
            FilterRocket.FALCON_HEAVY -> Rocket.FALCON_HEAVY
            FilterRocket.ATLAS_5 -> Rocket.ATLAS_5
        }
    }

    override fun map(rocketId: Long?): Int {
        return toDrawableRes(rocketId)
    }

    override fun invoke(rocketId: Long?): Int {
        return toStringRes(rocketId)
    }

    override fun invoke(rocket: FilterRocket): Long {
        return toDomainRocket(rocket)
    }

}

object X : MapFilterRocketToDomainRocket {
    override fun invoke(rocket: FilterRocket): Long {
        TODO("Not yet implemented")
    }

}