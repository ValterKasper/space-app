package sk.kasper.domain.usecase.impl

import org.threeten.bp.LocalDateTime
import sk.kasper.domain.usecase.GetCurrentLocalDateTime
import javax.inject.Inject

class GetCurrentLocalDateTimeImpl @Inject constructor() : GetCurrentLocalDateTime {

    override fun invoke(): LocalDateTime {
        return LocalDateTime.now()
    }

}