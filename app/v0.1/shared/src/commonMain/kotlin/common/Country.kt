package my.life.common

import kotlinx.datetime.TimeZone
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

/** ISO 3166-1 alpha-2 */
@JvmInline
@Serializable
value class Country(val code: String) {
    val defaultLanguage get() = defaultLanguages.getOrElse(code) { "en" }
    val defaultZone get() = TimeZone.of(defaultZones.getValue(code))
    init {
        syntax.mustMatch(code)
    }
    companion object {
        private val syntax = Regex("^[A-Z]{2}$")
        private val defaultLanguages = mapOf(
            "FI" to "fi",
            "SE" to "sv"
        )
        private val defaultZones = mapOf(
            "FI" to "Europe/Helsinki",
            "SE" to "Europe/Stockholm",
            "US" to "America/New_York"
        )
    }
}