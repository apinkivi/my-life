package my.life.time

import kotlinx.datetime.LocalTime
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

const val SECONDS_IN_DAY = 24 * 60 * 60

val Int.pad get() = toString().padStart(2, '0')

val LocalTime.inWholeMinutes get() = hour * 60 + minute

fun LocalTime.duration(to: LocalTime) = (to.inWholeMinutes - inWholeMinutes).minutes

operator fun LocalTime.plus(duration: Duration): LocalTime {
    var seconds = toSecondOfDay() + duration.inWholeSeconds.toInt()
    while (seconds < 0) seconds += SECONDS_IN_DAY
    return LocalTime.fromSecondOfDay(seconds % SECONDS_IN_DAY)
}

operator fun LocalTime.minus(duration: Duration) = plus(-duration)

