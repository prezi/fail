package com.prezi.fail.api.period

import org.junit.Test
import org.joda.time.DateTime
import org.joda.time.Duration

class SanFranciscoWeekdayWorktimeDailyTest: FailPeriodWithTimezoneTest {
    override val period = SanFranciscoWeekdayWorktimeDaily{Duration.standardHours(3)}

    Test fun nextRuns() {
        assertNextRuns(
                dt("2014-10-17T08:13:07"),  // Friday
                dt("2014-10-23T05:00:00"),  // Next Thursday

                dt("2014-10-20T13:00:00"),
                dt("2014-10-21T13:00:00"),
                dt("2014-10-22T13:00:00")
        )

        assertNextRuns(
                dt("2014-01-01T00:00:00"),
                dt("2014-01-02T13:01:00"),

                dt("2014-01-02T13:00:00")
        )
    }
}

