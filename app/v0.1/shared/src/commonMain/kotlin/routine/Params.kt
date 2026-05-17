package my.life.routine

import kotlinx.serialization.Serializable
import my.life.common.Country
import my.life.time.Date
import my.life.time.hour

/** User defined parameters. */
@Serializable
class Params(
    var location: Country,
    val birth: Date
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