package my.life.common

fun Regex.mustMatch(input: CharSequence) =
    if (matches(input)) input else throw IllegalArgumentException("'$input' isn't valid!")