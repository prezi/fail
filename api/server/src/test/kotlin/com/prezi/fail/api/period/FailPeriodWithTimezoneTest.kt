package com.prezi.fail.api.period

import org.joda.time.DateTime

trait FailPeriodWithTimezoneTest: FailPeriodTest {
    override val period: FailPeriodWithTimezone

    protected fun dt(o: Any): DateTime = DateTime(o, period.tz)
}
