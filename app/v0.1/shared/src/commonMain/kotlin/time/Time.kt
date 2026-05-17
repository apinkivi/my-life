package my.life.time

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/** Time using local timezone. */
@Serializable(with = Time.Serializer::class)
sealed interface Time : Comparable<Time> {
    object Serializer : KSerializer<Time> {
        override val descriptor = PrimitiveSerialDescriptor("Time", PrimitiveKind.STRING)
        override fun serialize(encoder: Encoder, value: Time) = encoder.encodeString(value.serialized)
        override fun deserialize(decoder: Decoder) = of(decoder.decodeString())
    }
    val serialized get() = toString()
    override fun compareTo(other: Time): Int = TODO()
    operator fun plus(other: TemporalAmount): Time = TODO()
    operator fun minus(other: TemporalAmount): Time = TODO()
    companion object {
        fun of(input: String) = when (input.length) {
            1 -> Day.of(input)                                // 1    known
            2 -> Month.of(input)                                    // 01   known   TODO Prefer
            3 -> Week(input)                                        // W01  known   TODO Prefer
            4 -> when {
                Month.match(input) -> Month.of(input)               // --01, deprecated TODO postpone
                Week.match(input) -> Week(input)                    // -W01, unknown    TODO postpone
                DayOfYear.match(input) -> DayOfYear(input)          // -001, deprecated
                Day.match(input) -> Day.of(input)       // -W-1, deprecated, not serialized
                else -> Year(input)                                 // 1970, standard
            }
            5 -> if (DayNo.match(input)) DayNo(input)     // ---01, deprecated
                else Clock(input)                               // 00:00, standard  TODO T-handling
            6 -> WeekAndDay(input)                                  // -W01-1, deprecated
            7 -> if (MonthAndDay.match(input)) MonthAndDay(input)   // --01-01, deprecated
                else YearAndMonth(input)                            // 1970-01, standard
            8 -> if (YearAndWeek.match(input)) YearAndWeek(input)   // 1970-W01, standard
                else YearAndDay(input)                              // 1970-001, standard
            10 -> Date(input)                                       // 1970-01-01, standard
            16 -> DateTime(input)                                   // 1970-01-01T00:00, standard
            else -> throw IllegalArgumentException("Unable to parse time of '$input'!")
        }
    }
}
