package my.life.common

fun <T> MutableList<T>.replaceOrAdd(newElement: T, predicate: (T) -> Boolean) {
    val index = indexOfFirst(predicate)
    if (index >= 0) this[index] = newElement else add(newElement)
}