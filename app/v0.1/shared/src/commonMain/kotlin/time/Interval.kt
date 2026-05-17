package my.life.time

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/**
 * https://en.wikipedia.org/wiki/ISO_8601 Time interval
 *
 * Users usually want to see start and end time, but it's better to save duration than end time,
 * because of time zones and day changes.
 */
@Serializable(with = Interval.Serializer::class)
sealed interface Interval {
    object Serializer : KSerializer<Interval> {
        override val descriptor = PrimitiveSerialDescriptor("Interval", PrimitiveKind.STRING)
        override fun serialize(encoder: Encoder, value: Interval) = encoder.encodeString(value.toString())
        override fun deserialize(decoder: Decoder) = parse(decoder.decodeString())
    }
    val start: Time
    val duration: Duration
    val end: Time
    val startAndEnd get() = "$start/$end"
    val startAndDuration get() = "$start/$duration"
    val durationAndEnd get() = "$duration/$end"
    companion object {
        fun parse(value: String): Interval {
            val start = Time.of(value.substringBefore('/'))
            val duration = Duration.of(value.substringAfter('/'))
            return StartAndDuration(start, duration)
        }
    }
}
