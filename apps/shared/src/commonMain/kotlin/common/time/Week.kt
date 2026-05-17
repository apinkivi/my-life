package my.life.common.time

import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.DayOfWeek.*
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus
import kotlinx.datetime.until
import my.life.common.locale.*

/** Week https://en.m.wikipedia.org/wiki/Week */
data class Week(val number: Int, val system: System) {
    val firstDay get() = system.firstDayOfWeek

    /** Week numbering system https://en.m.wikipedia.org/wiki/Week#Numbering */
    enum class System(val firstDayOfWeek: DayOfWeek, val minimalDaysInFirstWeek: Int = 1) {
        ISO(MONDAY, 4),
        MIDDLE_EAST(SATURDAY),
        WESTERN(SUNDAY);

        fun weekOf(date: LocalDate) : Week {
            val startOfFirstWeek = LocalDate(date.year, 1, minimalDaysInFirstWeek).firstDayInWeek
            val number = startOfFirstWeek.until(date, DateTimeUnit.WEEK) + 1
            return Week(number.toInt(), this)
        }

        val LocalDate.firstDayInWeek get() =
            minus((dayOfWeek.isoDayNumber - firstDayOfWeek.isoDayNumber).mod(7), DateTimeUnit.DAY)

        companion object {
            /** Get week numbering system by country code. */
            fun of(code: String) = when (code) {
                CANADA, UNITED_STATES_OF_AMERICA -> WESTERN
                SAUDI_ARABIA, UNITED_ARAB_EMIRATES -> MIDDLE_EAST
                else -> ISO
            }
        }
    }
}