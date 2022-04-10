package sk.kasper.domain.usecase

import org.threeten.bp.LocalDateTime

internal fun interface GetCurrentLocalDateTime {

    operator fun invoke(): LocalDateTime

}