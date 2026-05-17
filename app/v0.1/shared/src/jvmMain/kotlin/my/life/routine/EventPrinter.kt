package my.life.routine

import fi.life.my.life.time.ClockFormatter
import my.life.routine.Event.Type.End
import my.life.routine.Event.Type.Post
import my.life.routine.Event.Type.Pre
import my.life.routine.Event.Type.Start
import my.life.time.Clock
import my.life.time.Day
import my.life.time.Time
import my.life.time.hour
import java.util.ResourceBundle

class EventPrinter(val config: Config) {
    val locale = config.params.locale
    val bundle = ResourceBundle.getBundle("Routines", locale)!!
    val formatter = ClockFormatter(locale)
    val maxLength = formatter.format(11.hour).length

    fun format(time: Time) = formatter.format(time as Clock).padStart(maxLength)

    val String.local: String get() = if (bundle.containsKey(this)) bundle.getString(this) else this

    fun printPlan(day: Day) = config.weekly.plan()[day].events.joinToString("\n") { it.item }

    val Event.item get() = "${format(time)} ${when (type) {
        Pre -> '('
        Start -> '<'
        End -> '>'
        Post -> ')'
    }} ${routine.id.local}"
}