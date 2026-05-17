package my.life.routine

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import my.life.time.Day
import my.life.time.Time
import my.life.time.hour
import my.life.time.minutes
import kotlin.jvm.JvmInline

@Serializable
sealed interface TimeFormula {
    fun count(config: Config, daily: DailyPlan): Time
}

@Serializable
@SerialName("fixed")
@JvmInline
value class FixedTime(val time: Time): TimeFormula {
    override fun count(config: Config, daily: DailyPlan) = time
}

@Serializable
@SerialName("beforeGroup")
@JvmInline
value class BeforeGroup(val group: String) : TimeFormula {
    override fun count(config: Config, daily: DailyPlan) = (daily.groupStart(group)
        ?: throw NoSuchElementException("${daily.day} doesn't have '$group' group!")) - 1.minutes
}

@Serializable
@SerialName("beforeRoutine")
@JvmInline
value class BeforeRoutine(val routine: String) : TimeFormula {
    override fun count(config: Config, daily: DailyPlan) = (daily.routineStart(routine)
        ?: throw NoSuchElementException("${daily.day} doesn't have '$routine' routine!")) - 1.minutes
}