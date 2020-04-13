package sk.kasper.space

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.threeten.bp.*
import org.threeten.bp.format.TextStyle
import org.threeten.bp.temporal.ChronoUnit
import org.threeten.bp.temporal.TemporalAdjusters
import java.util.*

class JavaTimeTest {

    @Test
    fun localDate_compareDates() {
        val now = LocalDate.now()
        val tomorrow = now.plusDays(1)

        assertTrue(tomorrow.isAfter(now))
    }

    @Test
    fun localDate_adjustment() {
        val date = LocalDate.of(2017, Month.NOVEMBER, 16)
        assertTrue(date.dayOfWeek == DayOfWeek.THURSDAY)

        val nextMonday = date.with(TemporalAdjusters.next(DayOfWeek.MONDAY))
        assertTrue(nextMonday.dayOfMonth == 20)
    }

    @Test
    fun localDateTime_midnightOfToday() {
        val night = LocalDateTime.of(2017, Month.NOVEMBER, 16, 19, 12)
        val midnight = night
                .plusDays(1)
                .withHour(0)
                .withSecond(0)

        assertTrue(night.isBefore(midnight))
        val tomorrowNight = night.plusDays(1)
        assertTrue(midnight.isBefore(tomorrowNight))
    }

    @Test
    fun dayOfWeek_getDisplayName() {
        val friday = DayOfWeek.FRIDAY

        assertEquals(friday.getDisplayName(TextStyle.FULL, Locale.ENGLISH), "Friday")
        assertEquals(friday.getDisplayName(TextStyle.NARROW, Locale.ENGLISH), "F")
    }

    @Test
    fun yearMonth_lengthOfMonth() {
        val november2017 = YearMonth.of(2017, Month.NOVEMBER)

        assertTrue(november2017.lengthOfMonth() == 30)
    }

    @Test
    fun zonedDateTime_fromLA_toTokyo() {
        val departureDateTime = LocalDateTime.of(2017, Month.NOVEMBER, 16, 20, 0)
        val departureZoneId = ZoneId.of("America/Los_Angeles")
        val departureInLaZone = ZonedDateTime.of(departureDateTime, departureZoneId)

        val arrivingZoneId = ZoneId.of("Asia/Tokyo")
        val departureInTokyoZone = departureInLaZone.withZoneSameInstant(arrivingZoneId)
        val arrivingInTokyoZone = departureInTokyoZone
                .plusHours(10)
                .plusMinutes(50) // 650 min

        assertTrue(departureInLaZone.toInstant() == departureInTokyoZone.toInstant())
        assertTrue(arrivingInTokyoZone.isAfter(departureInLaZone))
    }

    @Test
    fun instant_plus_and_until() {
        val epoch = Instant.ofEpochMilli(0)
        val plus30SecondsFromEpoch = epoch.plusSeconds(30)

        assertTrue(epoch.until(plus30SecondsFromEpoch, ChronoUnit.SECONDS) == 30L)
    }

    @Test
    fun period_daysBetween() {
        val localDate = LocalDate.of(2017, Month.NOVEMBER, 16)
        val christmasDate = LocalDate.of(2017, Month.DECEMBER, 24)
        val period = Period.between(localDate, christmasDate)

        assertEquals(1, period.months)
        assertEquals(8, period.days)
    }

    @Test
    fun chronoUnit_daysBetween() {
        val localDate = LocalDate.of(2017, Month.NOVEMBER, 16)
        val christmasDate = LocalDate.of(2017, Month.DECEMBER, 24)
        val days = ChronoUnit.DAYS.between(localDate, christmasDate)

        assertEquals(38, days)
    }
}