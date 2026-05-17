package my.life.common.time

import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.todayIn
import kotlin.time.Clock
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import kotlin.time.ExperimentalTime

const val SECONDS_IN_DAY = 24 * 60 * 60

val Int.day get() = DayOfWeek(this)
val Int.hour get() = LocalTime(this, 0)

fun Int.hour(minute: Int) = LocalTime(this, minute)

val LocalTime.inWholeMinutes get() = hour * 60 + minute

val LocalDate.startOfWeek get() = minus(dayOfWeek.ordinal, DateTimeUnit.DAY)

fun LocalDate.age(timeZone: TimeZone) = timeZone.today.let {
    it.year - year - if (it < LocalDate(it.year, month, day)) 1 else 0
}

@OptIn(ExperimentalTime::class)
fun LocalDateTime.plus(duration: Duration, timeZone: TimeZone) = (toInstant(timeZone) + duration).toLocalDateTime(timeZone)
fun LocalDateTime.minus(duration: Duration, timeZone: TimeZone) = plus(-duration, timeZone)

fun LocalTime.duration(to: LocalTime) = (to.inWholeMinutes - inWholeMinutes).minutes

operator fun LocalTime.plus(duration: Duration): LocalTime {
    var seconds = toSecondOfDay() + duration.inWholeSeconds.toInt()
    while (seconds < 0) seconds += SECONDS_IN_DAY
    return LocalTime.fromSecondOfDay(seconds % SECONDS_IN_DAY)
}

operator fun LocalTime.minus(duration: Duration) = plus(-duration)

