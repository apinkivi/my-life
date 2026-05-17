package my.life.routine

import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.plus
import my.life.common.time.now
import my.life.common.time.startOfWeek
import my.life.common.time.today
import kotlin.time.ExperimentalTime

class WeekPlan(val config: Config) {
    @OptIn(ExperimentalTime::class)
    val created = now
    val start = config.params.timeZone.today.startOfWeek
    var planned = false
    private val dayPlans = DayOfWeek.entries.associateWith {
        DayPlan(config, start.plus(it.ordinal, DateTimeUnit.DAY))
    }

    fun plan(): WeekPlan {
        if (!planned) {
            config.routines.daily.forEach { routine -> routine.days.forEach { get(it) + routine } }
            planned = true
        }
        return this
    }

    operator fun get(day: DayOfWeek) = dayPlans.getValue(day)
}