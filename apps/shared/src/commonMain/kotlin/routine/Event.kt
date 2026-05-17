package my.life.routine

import my.life.routine.Event.Type.*
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
data class Event(val routine: Routine, val time: Instant, val type: Type) : Comparable<Event> {
    enum class Type { Pre, Start, End, Post }
    override fun compareTo(other: Event) = time.compareTo(other.time)
    override fun toString() = "$time ${when (type) {
        Pre -> '('
        Start -> '<'
        End -> '>'
        Post -> ')'
    }} ${routine.id}}"
}