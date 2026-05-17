package my.life.routine

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
//import kotlinx.datetime.minus
import kotlin.jvm.JvmInline
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@Deprecated("Replaced by fixed property")
@Serializable
@OptIn(ExperimentalTime::class)
sealed interface TimeFormula {
    fun count(config: Config, daily: DayPlan): Instant
}

@Deprecated("Replaced by fixed property")
@Serializable
@SerialName("fixed")
@JvmInline
@OptIn(ExperimentalTime::class)
value class FixedTime(val time: Instant): TimeFormula {
    override fun count(config: Config, daily: DayPlan) = time
}

@Deprecated("Replaced by fixed property")
@Serializable
@SerialName("beforeGroup")
@JvmInline
@OptIn(ExperimentalTime::class)
value class BeforeGroup(val group: String) : TimeFormula {
    override fun count(config: Config, daily: DayPlan) = TODO()
        /*(daily.groupStart(Routine.Group.valueOf(group))
        ?: throw NoSuchElementException("${daily.day} doesn't have '$group' group!")).minus(1.minutes.toDateTimePeriod())*/
}

@Deprecated("Replaced by fixed property")
@Serializable
@SerialName("beforeRoutine")
@JvmInline
@OptIn(ExperimentalTime::class)
value class BeforeRoutine(val routine: String) : TimeFormula {
    override fun count(config: Config, daily: DayPlan) = TODO()/*(daily.routineStart(routine)
        ?: throw NoSuchElementException("${daily.day} doesn't have '$routine' routine!")) - 1.minutes.toDateTimePeriod()*/
}