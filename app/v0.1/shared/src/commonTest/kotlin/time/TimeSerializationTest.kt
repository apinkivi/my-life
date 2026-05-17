package my.life.time

import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

class TimeSerializationTest {
    @Test fun dayOfWeek() = test("-W-1", "1")

    companion object {
        fun test(actual: String, expected: String = actual) {
            var time = actual.time
            var json = Json.encodeToString(time)
            val quoted = "\"$expected\""
            assertEquals(quoted, json)
            time = Json.decodeFromString(json)
            json = Json.encodeToString(time)
            assertEquals(quoted, json)
        }
    }
}