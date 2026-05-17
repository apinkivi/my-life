package my.life.routine

import my.life.routine.Event.Type.*
import my.life.time.Time

data class Event(val routine: Routine, val time: Time, val type: Type) : Comparable<Event> {
    enum class Type { Pre, Start, End, Post }
    override fun compareTo(other: Event) = time.compareTo(other.time)
    override fun toString() = "$time ${when (type) {
        Pre -> '('
        Start -> '<'
        End -> '>'
        Post -> ')'
    }} ${routine.id}}"
}