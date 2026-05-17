package my.life.routine

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import my.life.common.replaceOrAdd
import my.life.time.Day
import my.life.time.Duration
import my.life.time.Interval
import my.life.time.Time
import my.life.time.div
import my.life.routine.Routine.RepeatBase
import my.life.routine.Routine.RepeatBase.*
import my.life.time.Clock
import kotlin.collections.set

/** Routine configuration which is the base of routines. */
@Serializable
class Config(val params: Params) {
    val routines = mutableListOf<Routine>()

    @Transient private var default = true
    @Transient private var groupId: String? = null
    @Transient private var beforeGroupId: String? = null
    @Transient private var beforeRoutineId: String? = null
    @Transient private var reversed = false

    val age get() = params.birth.age(params.timeZone)
    val man get() = params.man == true
    val woman get() = params.man == false
    val townHouse get() = params.townHouse

    //#region Routine definers

    fun group(id: String, init: Config.() -> Unit) {
        groupId = id
        init()
        groupId = null
    }

    fun groupReversed(id: String, before: String, init: Config.() -> Unit) = group(id) {
        reversed = true
        beforeGroupId = before
        init()
        beforeGroupId = null
        reversed = false
    }

    fun daily(id: String, priority: String, group: String? = groupId, init: Routine.() -> Unit = {}) =
        routine(id, Daily, priority, group, init)
    fun daily(id: String, priority: String, duration: Duration, init: Routine.() -> Unit = {}) =
        routine(id, Daily, priority, groupId) {
            this.duration = duration
            init()
        }
    fun daily(id: String, priority: String, start: Time, duration: Duration, init: Routine.() -> Unit = {}) =
        daily(id, priority, groupId, start, duration, init)
    fun daily(id: String, priority: String, group: String? = groupId, start: Time, duration: Duration, init: Routine.() -> Unit = {}) =
        daily(id, priority, group) {
            this.startFormula = FixedTime(start)
            this.duration = duration
        init()
    }

    fun weekly(id: String, priority: String, group: String? = groupId, init: Routine.() -> Unit = {}) =
        routine(id, Weekly, priority, group, init)

    fun monthly(id: String, priority: String, init: Routine.() -> Unit = {}) =
        routine(id, Monthly, priority, init = init)

    fun yearly(id: String, priority: String, init: Routine.() -> Unit = {}) =
        routine(id, Yearly, priority, init = init)

    private fun routine(id: String, base: RepeatBase, priority: String, group: String? = groupId, init: Routine.() -> Unit = {}) =
        Routine(id, base, priority, default, group).apply {
            beforeGroupId?.let {
                endFormula = BeforeGroup(it)
                beforeGroupId = null
            }
            beforeRoutineId?.let {
                endFormula = BeforeRoutine(it)
                beforeRoutineId = null
            }
            if (reversed) beforeRoutineId = id
            init(this)
            routines.replaceOrAdd(this) { it.id == id }
        }

    private fun disable(id: String) {}

    fun defaultsSet() { default = false }

    //#endregion

    //#region Calculations

    @Transient
    val weekly = WeeklyPlan(this)

    /** Activity (work, study, weekend) start time. */
    //val Day.start get() = working.getValue(this).start as Clock

    //#endregion
}