/**
 * - clock time (12:00, 12:32)
 * - day of week (Monday, Tuesday, ...)
 * - TODO day of month from start (1., 2., ...)
 * - TODO day of month from end (last, 2nd last, ...)
 * - half year (H1, H2)
 * - month (January, February)
 * - month and day (--01-01, --01-02, ...)
 * - quarter (of year) (Q1, Q2, Q3, Q4)
 * - season (Winter, Spring, Summer, Autumn)
 * - time of day (Morning, Day, Evening, Night)
 * - week and day (W01-1, W01-2, ...)
 * - TODO week of month (0. < week, 1. full week, 2. full week, ...)
 * - TODO week of month and day (of week)
 * - week (of year) (W01, W02, ... ISO week number)
 */
package my.life.time

import kotlinx.datetime.LocalTime
import kotlinx.datetime.number
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline
import my.life.time.Month.*
import my.life.time.Quarter.*

interface RelativeTime : Time

/** Clock time */
@Serializable
@JvmInline
value class Clock(val value: LocalTime) : RelativeTime {
    constructor(input: CharSequence) : this(LocalTime.parse(input))
    constructor(hour: Int = 0, minute: Int = 0) : this(LocalTime(hour, minute))

    override fun compareTo(other: Time) = value.compareTo((other as Clock).value)
    override fun plus(other: TemporalAmount) = (value + (other as Duration).wrapped).wrap
    override fun minus(other: TemporalAmount) = (value - (other as Duration).wrapped).wrap
    override fun toString() = value.toString()
}

/** Day of month. */
@JvmInline
value class DayNo(val value: Int = 1) : RelativeTime {
    constructor(input: String) : this(input.removePrefix(PREFIX).toInt())
    override fun toString() = format(value)
    companion object {
        const val PREFIX = "---"
        fun format(number: Int) = PREFIX + number.pad
        fun match(input: String) = input.startsWith(PREFIX)
    }
}

/** Day of week numbers are standard even week start may change */
enum class Day : RelativeTime {
    Monday, Tuesday, Wednesday, Thursday, Friday, Saturday, Sunday;
    val number get() = ordinal + 1
    override val serialized get() = format1(number)
    val wrapped get() = kotlinx.datetime.DayOfWeek.entries[ordinal]
    operator fun dec() = if (this == Monday) Sunday else entries[ordinal - 1]
    operator fun inc() = if (this == Sunday) Monday else entries[ordinal + 1]
    companion object {
        const val PREFIX = "-W-"
        fun format1(number: Int) = number.toString()
        fun format4(number: Int) = PREFIX + number
        fun match(input: String) = input.startsWith(PREFIX)
        fun of(input: String) = of(input.removePrefix(PREFIX).toInt())
        fun of(number: Int) = Day.entries[number - 1]
        fun of(kotlinx: kotlinx.datetime.DayOfWeek) = Day.entries[kotlinx.ordinal]
    }
}

enum class HalfYear(vararg val quarters: Quarter) : RelativeTime {
    H1(Q1, Q2),
    H2(Q3, Q4);
    val months get() = quarters.flatMap { it.months.asIterable() }
}

enum class Month : RelativeTime {
    January, February, March, April, May, June, July, August, September, October, November, December;
    val half get() = HalfYear.entries.first { it.months.contains(this) }
    val number get() = wrapped.number
    val quarter get() = Quarter.entries.first { it.months.contains(this) }
    val season get() = Season.entries.single { it.months.contains(this) }
    val wrapped get() = kotlinx.datetime.Month.entries[ordinal]
    companion object {
        const val PREFIX = "--"
        fun format(month: Month) = format(month.number)
        fun format(number: Int) = number.pad
        fun match(input: String) = input.startsWith(PREFIX)
        fun of(number: Int) = entries[number - 1]
        fun of(input: String) = of(input.removePrefix(PREFIX).toInt())
        fun of(kotlinx: kotlinx.datetime.Month) = entries[kotlinx.ordinal]
    }
}

data class MonthAndDay(val month: Month = January, val day: DayNo = DayNo()) : RelativeTime {
    constructor(input: String) : this(Month.of(input.take(4)), DayNo(input.substring(5)))
    override fun toString() = format(month, day)
    companion object {
        fun format(month: Month, day: DayNo) = "$month-${day.value.pad}"
        fun match(input: String) = Month.match(input)
    }
}

enum class Quarter(vararg val months: Month) : RelativeTime {
    Q1(January, February, March),
    Q2(April, May, June),
    Q3(July, August, September),
    Q4(October, November, December);
    val half get() = HalfYear.entries.first { it.quarters.contains(this) }
}

enum class Season(
    val symbol: String,
    /** Months which may vary by culture. */
    vararg var months: Month
) : RelativeTime {
    Winter("❄️", December, January, February),
    Spring("🌱", March, April, May),
    Summer("🌻", June, July, August),
    Autumn("🍂", September, October, November);
}

enum class TimeOfDay(
    val symbol: String,
    /** Interval which may vary by culture. */
    var interval: Interval
) : RelativeTime {
    Morning("🌅", 6.hour / 12.hour),
    Day("☀️", 12.hour / 18.hour),
    Evening("🌇", 18.hour / 0.hour),
    Night("🌙", 0.hour / 6.hour);
}

@JvmInline
value class Week(val value: Int = 1) : RelativeTime {
    constructor(input: String) : this(input.removePrefix(PREFIX).toInt())
    override fun toString() = format(value)
    companion object {
        const val PREFIX = "-W" // TODO W
        fun format(value: Int) = PREFIX + value.pad
        fun match(input: String) = input.startsWith(PREFIX)
    }
}

data class WeekAndDay(val week: Week = Week(), val day: Day = Day.Monday) : RelativeTime {
    constructor(input: String) : this(Week(input.take(4)), Day.of(input.substring(5)))
    override fun toString() = format(week, day)
    companion object {
        fun format(week: Week, day: Day) = "$week-${day.number}"
    }
}