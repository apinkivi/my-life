package my.life.routine

import my.life.routine.Routine.RepeatBase.Daily
import my.life.time.Day

class WeeklyPlan(val config: Config) {
    var planned = false
    private val dayPlans = Day.entries.associateWith { DailyPlan(config, it) }

    fun plan(): WeeklyPlan {
        if (!planned) {
            val daily = config.routines.filter { it.base == Daily }
            daily.filter { it.group == ACTIVITY }.plan() // Plan activities first
            daily.filter { it.group == NIGHT }.plan()
            daily.filter { it.group != ACTIVITY && it.group != NIGHT }.plan() // Then rest
            planned = true
        }
        return this
    }

    private fun List<Routine>.plan() = forEach { routine -> routine.days.forEach { get(it) + routine } }

    operator fun get(day: Day) = dayPlans.getValue(day)
}