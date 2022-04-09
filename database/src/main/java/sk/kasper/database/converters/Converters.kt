package sk.kasper.database.converters

import androidx.room.TypeConverter
import org.threeten.bp.LocalDateTime
import sk.kasper.base.utils.toLocalDateTime
import sk.kasper.base.utils.toTimeStamp

class Converters {

    @TypeConverter
    fun toLocalDateTime(value: Long): LocalDateTime {
        return value.toLocalDateTime()
    }

    @TypeConverter
    fun toTimeStamp(value: LocalDateTime): Long {
        return value.toTimeStamp()
    }

}