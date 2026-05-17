package my.life.common.time

import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes

fun Int.hours(minutes: Int) = hours + minutes.minutes
