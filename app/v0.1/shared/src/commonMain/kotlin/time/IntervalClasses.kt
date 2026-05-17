package my.life.time

data class StartAndDuration(override val start: Time, override val duration: Duration) : Interval {
    override val end: Time get() = start + duration
    override fun toString() = startAndEnd
}

data class StartAndEnd(override val start: Time, override val end: Time) : Interval {
    override val duration: Duration get() = TODO("Not yet implemented")
    override fun toString() = startAndEnd
}