package my.life.routine

import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalTime
import kotlinx.serialization.Serializable
import my.life.routine.Event.Type.*
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@Serializable
data class Routine(
    /** Routine ID which may be DefaultRoutine.name, Definition of Done or link. */
    val id: String,
    val base: RepeatBase,
    /** Priority ID. Capitalized means default. */
    val priority: String,
    val default: Boolean = false,
    val group: Group? = null
) {
    enum class Group { AWAKENING, ACTIVITY, BEDTIME, NIGHT }
    enum class RepeatBase { DAILY, WEEKLY, MONTHLY, YEARLY }
    val daily get() = base == RepeatBase.DAILY
    /** Days to apply */
    var days = DayOfWeek.entries.toList()
    var travel: Duration? = null
    var after: String? = null
    /** Prefer after definition except defaults. */
    var before: String? = null
    var beforeGroup: Group? = null
    var beforeRoutine: String? = null
    var start: LocalTime? = null
    var startFormula: TimeFormula? = null
    var duration: Duration? = null
    var endFormula: TimeFormula? = null
    //var interval: Interval? = null

    fun days(vararg days: DayOfWeek) { this.days = days.toList() }

    @OptIn(ExperimentalTime::class)
    private fun event(time: Instant?, type: Event.Type) = time?.let { Event(this, it, type) }

    operator fun rangeTo(other: Routine): Routine {
        before = other.id
        other.after = id
        return other
    }

    fun plan(config: Config, daily: DayPlan) {
        val shiftToYesterday = endFormula != null
        var pre: LocalTime? = null
        var start = null //startFormula?.count(config, daily)
        var end = null //endFormula?.count(config, daily)
        var post: LocalTime? = null
        /*travel?.let {
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
        daily + event(post, Post)*/
    }
}