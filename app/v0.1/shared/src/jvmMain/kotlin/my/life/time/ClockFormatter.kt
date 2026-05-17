package fi.life.my.life.time

import kotlinx.datetime.toJavaLocalTime
import my.life.time.Clock
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale

class ClockFormatter(locale: Locale, style: FormatStyle = FormatStyle.SHORT) {
    val formatter = DateTimeFormatter.ofLocalizedTime(style).withLocale(locale)!!

    fun format(time: Clock) = formatter.format(time.value.toJavaLocalTime())!!
}