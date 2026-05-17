package my.life.common.locale

import kotlinx.datetime.TimeZone
import kotlinx.serialization.Serializable
import my.life.common.mustMatch
import my.life.common.time.Week
import kotlin.jvm.JvmInline

/** ISO 3166-1 alpha-2 */
@JvmInline
@Serializable
value class Country(val code: String) {
    val defaultLanguage get() = defaultLanguages.getOrElse(code) { "en" }
    val defaultZone get() = TimeZone.Companion.of(defaultZones.getValue(code))
    val weekSystem get() = Week.System.of(code)
    init {
        syntax.mustMatch(code)
    }
    companion object {
        private val syntax = Regex("^[A-Z]{2}$")
        private val defaultLanguages = mapOf(
            FINLAND to "fi",
            SWEDEN to "sv"
        )
        private val defaultZones = mapOf(
            FINLAND to "Europe/Helsinki",
            SWEDEN to "Europe/Stockholm",
            UNITED_STATES_OF_AMERICA to "America/New_York"
        )
    }
}