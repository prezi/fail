package com.prezi.fail.api.period

import org.joda.time.Interval
import kotlin.test.assertEquals
import org.joda.time.DateTime

trait FailPeriodTest {
    open val period: FailPeriod

    fun assertNextRun(after: Long, expected: Long) {
        assertEquals(DateTime(expected*1000), period.nextRun(DateTime(after*1000)))
    }

    fun assertNextRuns(after: DateTime, before: DateTime, vararg expected: DateTime) {
        assertEquals(expected.toList(), period.nextRuns(Interval(after, before)))
    }
}
