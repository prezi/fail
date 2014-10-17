package com.prezi.fail.api.period

import org.joda.time.DateTime
import org.joda.time.Interval
import org.joda.time.Period
import org.joda.time.Duration

abstract class FailPeriod(val offset: (Period) -> Duration) {
    protected fun chooseFromInterval(interval: Interval): DateTime =
            interval.getStart()!!.plus(offset(Period(interval)))!!

    public fun nextRun(after: DateTime): DateTime = chooseFromInterval(nextInterval(after))

    public fun nextRuns(between: Interval): List<DateTime> =
            stream(
                    nextInterval(between.getStart()!!),
                    {nextInterval(it.getEnd())}
            ).map{chooseFromInterval(it)}
             .takeWhile{between.contains(it)}
             .toList()

    protected abstract fun nextInterval(after: DateTime): Interval
    abstract val description: String
}
