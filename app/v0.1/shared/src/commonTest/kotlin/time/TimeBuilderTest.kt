package my.life.time

import kotlin.test.Test
import kotlin.test.assertEquals

class TimeBuilderTest {
    @Test fun date_epoch() = assertEquals("1970-01-01", date().toString())
    @Test fun date_me() = assertEquals("1983-05-04", date(1983, 5, 4).toString())

    @Test fun dayOfWeek() = assertEquals("Monday", 1.day.toString())

    @Test fun time() = assertEquals("12:34", "12:34".time.toString())
}