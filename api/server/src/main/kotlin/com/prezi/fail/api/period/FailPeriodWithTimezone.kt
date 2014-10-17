package com.prezi.fail.api.period

import org.joda.time.Duration
import org.joda.time.DateTimeZone
import org.joda.time.Period
import org.joda.time.Interval
import org.joda.time.DateTimeConstants
import org.joda.time.DateTime

abstract class FailPeriodWithTimezone(offset: (Period) -> Duration): FailPeriod(offset) {
    abstract val tz: DateTimeZone
}

