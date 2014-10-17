package com.prezi.fail.api.period

import org.junit.Test
import kotlin.test.assertEquals
import org.joda.time.DateTime
import org.joda.time.Interval
import org.joda.time.Duration

class EveryHourTest: FailPeriodTest {
    override val period = EveryHour{Duration(42000)}

    Test fun nextRun() {
        assertNextRun(0, 3642)
        assertNextRun(60, 3642)
        assertNextRun(3599, 3642)
        assertNextRun(3600, 7242)
        assertNextRun(3620, 7242)
        assertNextRun(3900, 7242)
    }

    Test fun nextRuns() {
        assertNextRuns(
                DateTime("2014-01-01T00:00:00"),
                DateTime("2014-01-01T05:00:00"),

                DateTime("2014-01-01T01:00:42"),
                DateTime("2014-01-01T02:00:42"),
                DateTime("2014-01-01T03:00:42"),
                DateTime("2014-01-01T04:00:42")
        )
    }
}