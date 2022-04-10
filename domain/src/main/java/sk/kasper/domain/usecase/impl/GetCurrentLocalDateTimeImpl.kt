package sk.kasper.domain.usecase.impl

import org.threeten.bp.LocalDateTime
import sk.kasper.domain.usecase.GetCurrentLocalDateTime

class GetCurrentLocalDateTimeImpl : GetCurrentLocalDateTime {

    override fun invoke(): LocalDateTime {
        return LocalDateTime.now()
    }

}