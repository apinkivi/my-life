package my.life.routine

import kotlinx.datetime.LocalDate
import my.life.common.minus
import my.life.common.time.minus
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class DayPlan(val config: Config, val start: LocalDate) {
    val day get() = start.dayOfWeek
    val events = mutableListOf<Event>()

    fun groupStart(group: Routine.Group) = events.firstOrNull { it.routine.group == group }?.time

    fun routineStart(routine: String) = events.firstOrNull { it.routine.id == routine }?.time

    operator fun dec() = config.weekly[start.dayOfWeek - 1]

    operator fun plus(event: Event?) = event?.let { events.add(it) }

    operator fun plus(routine: Routine) {
        routine.plan(config, this)
        events.sort()
        //TODO remove if (routine.activity) start = events.first { it.routine.activity }.time as Clock
    }
}