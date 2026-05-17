package my.life.routine

import kotlinx.serialization.json.Json
import kotlin.test.Test

class ConfigTest {
    private val json = Json { prettyPrint = true }

    @Test
    fun serialize() {
        println(json.encodeToString(jukka))
    }
}