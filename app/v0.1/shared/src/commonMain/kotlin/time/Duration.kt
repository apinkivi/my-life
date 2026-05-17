package my.life.time

import kotlinx.serialization.Serializable
import kotlin.time.Duration as XDuration
//import kotlinx.datetime.DatePeriod
//import kotlinx.datetime.DateTimePeriod

/** Same as Kotlin Duration, but doesn't care smaller than minutes and may also have weeks, months and years. */
@Serializable // TODO own serializer
data class Duration(val wrapped: XDuration) : Comparable<Duration>, TemporalAmount {
    override fun compareTo(other: Duration) = wrapped.compareTo(other.wrapped)
    companion object {
        fun of(value: String) = Duration(XDuration.parse(value))
    }
}