package com.prezi.fail.api.period

import org.joda.time.Duration
import org.joda.time.Period
import org.joda.time.DateTimeZone

open public class SanFranciscoWeekdayWorktimeDaily(offset: (Period) -> Duration): WeekdayWorktimeDaily(offset) {
    override val description = "Every weekday between 10AM and 4PM SF time"
    override val tz = DateTimeZone.forID("PST8PDT")
}
