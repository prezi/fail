package com.prezi.fail.period

import org.junit.Test
import kotlin.test.assertEquals

class EveryHourTest {
    val period = EveryHour()

    fun assertNextRun(after: Long, expected: Long) {
        assertEquals(expected, period.nextRun(after))
    }

    fun assertNextRuns(after: Long, before: Long, expected: Iterable<Long>) {
        assertEquals(
                expected.toList(),
                period.nextRuns(after, before)
        )
    }

    Test fun nextRun() {
        assertNextRun(0, 3600)
        assertNextRun(60, 3600)
        assertNextRun(3599, 3600)
        assertNextRun(3600, 7200)
        assertNextRun(3620, 7200)
    }

    Test fun nextRuns() {
        assertNextRuns(3900, 4*3600+500, (2*3600L..4*3600L step 3600))
        assertNextRuns(3600, 4*3600+500, (2*3600L..4*3600L step 3600))
        assertNextRuns(2*3600-1, 4*3600+500, (2*3600L..4*3600L step 3600))
    }
}