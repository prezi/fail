package com.prezi.fail.period

trait Period {
    public fun nextRun(after: Long): Long

    public fun nextRuns(after: Long, before: Long): List<Long> {
        val next = nextRun(after)
        return if (next >= before) {
            listOf()
        } else {
            listOf(next) + nextRuns(next, before)
        }
    }
}
