package my.life.routine

import kotlinx.serialization.Serializable
import my.life.common.put
import my.life.routine.Routine.Group.*

@Serializable
class Routines {
    val daily get() = dailyActivities + dailyAtNight + dailyRest
    private val dailyActivities = mutableListOf<Routine>()
    private val dailyAtNight = mutableListOf<Routine>()
    private val dailyRest = mutableListOf<Routine>()
    val rest = mutableListOf<Routine>()

    operator fun plus(routine: Routine) = with (routine) {
        (if (daily) when (group) {
            ACTIVITY -> dailyActivities
            NIGHT -> dailyAtNight
            else -> dailyRest
        } else rest).put(this) { it.id == id }
    }
}
