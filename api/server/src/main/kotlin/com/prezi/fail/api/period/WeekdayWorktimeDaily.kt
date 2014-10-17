package com.prezi.fail.api.period

import org.joda.time.Duration
import org.joda.time.Period
import org.joda.time.Interval
import org.joda.time.DateTime
import org.joda.time.DateTimeConstants

abstract public class WeekdayWorktimeDaily(offset: (Period) -> Duration)
: FailPeriodWithTimezone(offset) {
    override fun nextInterval(after: DateTime): Interval {
        val start = stream(
                DateTime(after, tz).plusDays(1)!!,
                {it.plusDays(1)!!}
        ).first{it.getDayOfWeek() <= DateTimeConstants.FRIDAY}
                .withTime(10, 0, 0, 0)!!
        val end = start.withTime(16, 0, 0, 0)
        return Interval(start, end)
    }
}