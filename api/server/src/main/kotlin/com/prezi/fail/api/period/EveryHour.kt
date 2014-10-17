package com.prezi.fail.api.period

import org.joda.time.DateTime
import org.joda.time.Period
import org.joda.time.Duration
import org.joda.time.Interval


public class EveryHour(offset: (Period) -> Duration) : FailPeriod(offset) {
    override val description = "Every hour"

    override fun nextInterval(after: DateTime): Interval {
        val start = after
                .withMinuteOfHour(0)!!
                .withSecondOfMinute(0)!!
                .withMillisOfSecond(0)!!
                .plusHours(1)!!
        val end = start.plusHours(1)!!.minusMillis(1)!!
        return Interval(start, end)
    }
}
