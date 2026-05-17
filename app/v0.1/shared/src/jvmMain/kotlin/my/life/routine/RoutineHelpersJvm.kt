package my.life.routine

import java.util.Locale

val Params.locale get() = Locale.of(language, location.code)!!