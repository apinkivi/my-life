package my.life.routine

import my.life.common.minus
import my.life.time.Clock
import my.life.time.Day

class DailyPlan(val config: Config, val day: Day) {
    /** Activity start of the day. */
    //TODO remove var start = config.params.dayStart

    val events = mutableListOf<Event>()

    fun groupStart(group: String) = events.firstOrNull { it.routine.group == group }?.time

    fun routineStart(routine: String) = events.firstOrNull { it.routine.id == routine }?.time

    operator fun dec() = config.weekly[day - 1]

    operator fun plus(event: Event?) = event?.let { events.add(it) }

    operator fun plus(routine: Routine) {
        routine.plan(config, this)
        events.sort()
        //TODO remove if (routine.activity) start = events.first { it.routine.activity }.time as Clock
    }
}