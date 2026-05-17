package my.life.common

/** Add or replace element if predicate matches. */
fun <T> MutableList<T>.put(element: T, predicate: (T) -> Boolean) {
    val index = indexOfFirst(predicate)
    if (index >= 0) this[index] = element else add(element)
}