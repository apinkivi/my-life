package my.life.routine

import kotlinx.serialization.Serializable
import my.life.routine.Event.Type.*
import my.life.time.Day
import my.life.time.Duration
import my.life.time.Time

@Serializable
data class Routine(
    /** Routine ID which may be DefaultRoutine.name, Definition of Done or link. */
    val id: String,
    val base: RepeatBase,
    /** Priority ID. Capitalized means default. */
    val priority: String,
    val default: Boolean = false,
    val group: String? = null
) {
    enum class RepeatBase { Daily, Weekly, Monthly, Yearly }
    val activity get() = group == ACTIVITY
    /** Days to apply */
    var days = Day.entries.toList()
    var travel: Duration? = null
    var after: String? = null
    /** Prefer after definition except defaults. */
    var before: String? = null
    var start: Time
        get() = (startFormula as FixedTime).time
        set(value) { startFormula = FixedTime(value) }
    var startFormula: TimeFormula? = null
    var duration: Duration? = null
    var endFormula: TimeFormula? = null
    //var interval: Interval? = null

    fun days(vararg days: Day) { this.days = days.toList() }

    private fun event(time: Time?, type: Event.Type) = time?.let { Event(this, it, type) }

    operator fun rangeTo(other: Routine): Routine {
        before = other.id
        other.after = id
        return other
    }

    fun plan(config: Config, daily: DailyPlan) {
        val shiftToYesterday = endFormula != null
        var pre: Time? = null
        var start = startFormula?.count(config, daily)
        var end = endFormula?.count(config, daily)
        var post: Time? = null
        travel?.let {
            if (endFormula is BeforeGroup || endFormula is BeforeRoutine) {
                post = end
                end = post?.minus(it)
            }
            pre = start?.minus(it)
            post = end?.plus(it)
        }
        duration?.let {
            if (start != null) end = start + it
            if (end != null) start = end - it
        }
        pre?.let {
            (if (shiftToYesterday && it > end!!) daily.dec() else daily) + event(pre, Pre)
        }
        daily + event(start, Start)
        daily + event(end, End)
        daily + event(post, Post)
    }
}