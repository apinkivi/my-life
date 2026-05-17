package my.life.routine

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable
import my.life.common.locale.Country
import my.life.common.time.hour

/** User defined parameters. */
@Serializable
class Params(
    var location: Country,
    val birth: LocalDate
) {
    /** Is Housing type town house or block of flats. */
    var townHouse = false
    var man: Boolean? = null

    var nationality = location
    var language = location.defaultLanguage
    var priorities = listOf(VITAL, HEALTH, WELFARE, EVERYDAY, ECONOMIC, FREE_TIME)
    var timeZone = location.defaultZone
    var dayStart = 9.hour
}