package my.life.common

inline operator fun <reified E : Enum<E>> E.plus(offset: Int): E {
    val entries = enumValues<E>()
    val newIndex = (ordinal + offset).floorMod(entries.size)
    return entries[newIndex]
}

inline operator fun <reified E : Enum<E>> E.minus(offset: Int): E = this + (-offset)

fun Int.floorMod(m: Int): Int = ((this % m) + m) % m
