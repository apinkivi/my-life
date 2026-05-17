package my.life.routine

import kotlinx.datetime.LocalTime
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import my.life.common.time.age
import my.life.routine.Routine.Group
import my.life.routine.Routine.RepeatBase
import my.life.routine.Routine.RepeatBase.*
import kotlin.time.Duration

/** Routine configuration which is the base of routines. */
@Serializable
class Config(val params: Params) {
    val routines = Routines()

    @Transient private var default = true
    @Transient private var currentGroup: Group? = null
    @Transient private var currentBeforeGroup: Group? = null
    @Transient private var currentBeforeRoutineId: String? = null
    @Transient private var reversed = false

    val age get() = params.birth.age(params.timeZone)
    val man get() = params.man == true
    val woman get() = params.man == false
    val townHouse get() = params.townHouse

    //#region Routine definers

    fun group(group: Group, init: Config.() -> Unit) {
        currentGroup = group
        init()
        currentGroup = null
    }

    fun groupReversed(group: Group, before: Group, init: Config.() -> Unit) = group(group) {
        reversed = true
        currentBeforeGroup = before
        init()
        currentBeforeGroup = null
        reversed = false
    }

    fun daily(id: String, priority: String, group: Group? = currentGroup, init: Routine.() -> Unit = {}) =
        routine(id, DAILY, priority, group, init)
    fun daily(id: String, priority: String, duration: Duration, init: Routine.() -> Unit = {}) =
        routine(id, DAILY, priority, currentGroup) {
            this.duration = duration
            init()
        }
    fun daily(id: String, priority: String, start: LocalTime, duration: Duration, init: Routine.() -> Unit = {}) =
        daily(id, priority, currentGroup, start, duration, init)
    fun daily(id: String, priority: String, group: Group? = currentGroup, start: LocalTime, duration: Duration, init: Routine.() -> Unit = {}) =
        daily(id, priority, group) {
            this.start = start
            this.duration = duration
        init()
    }

    fun weekly(id: String, priority: String, group: Group? = currentGroup, init: Routine.() -> Unit = {}) =
        routine(id, WEEKLY, priority, group, init)

    fun monthly(id: String, priority: String, init: Routine.() -> Unit = {}) =
        routine(id, MONTHLY, priority, init = init)

    fun yearly(id: String, priority: String, init: Routine.() -> Unit = {}) =
        routine(id, YEARLY, priority, init = init)

    private fun routine(id: String, base: RepeatBase, priority: String, group: Group? = currentGroup, init: Routine.() -> Unit = {}) =
        Routine(id, base, priority, default, group).apply {
            currentBeforeGroup?.let {
                beforeGroup = currentBeforeGroup
                currentBeforeGroup = null
            }
            currentBeforeRoutineId?.let {
                beforeRoutine = currentBeforeRoutineId
                currentBeforeRoutineId = null
            }
            if (reversed) currentBeforeRoutineId = id
            init(this)
            routines + this
        }

    private fun disable(id: String) {}

    fun defaultsSet() { default = false }

    //#endregion

    //#region Calculations

    @Transient
    val weekly = WeekPlan(this)

    /** Activity (work, study, weekend) start time. */
    //val Day.start get() = working.getValue(this).start as Clock

    //#endregion
}