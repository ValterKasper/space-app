package sk.kasper.ui_common.rocket

import androidx.annotation.DrawableRes

interface RocketMapper {

    @DrawableRes
    fun toDrawableRes(rocketId: Long?): Int

}