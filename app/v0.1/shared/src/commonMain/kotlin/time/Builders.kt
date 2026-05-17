package my.life.time

import kotlinx.datetime.LocalTime
import kotlin.time.Duration as KDuration
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes

val Int.day get() = Day.of(this)
val Int.hour get() = Clock(this)
val LocalTime.wrap get() = Clock(this)
fun Int.hour(minute: Int) = Clock(this, minute)

val KDuration.wrap get() = Duration(this)
val Int.hours get() = this.hours.wrap
val Int.minutes get() = this.minutes.wrap
fun Int.hours(minutes: Int) = (this.hours + minutes.minutes).wrap

val String.time get() = Time.of(this)

fun date(year: Int = Year.EPOCH, month: Int = 1, day: Int = 1) = Date(year, month, day)

operator fun Time.div(duration: Duration) = StartAndDuration(this, duration)
operator fun Time.div(end: Time) = StartAndEnd(this, end)

