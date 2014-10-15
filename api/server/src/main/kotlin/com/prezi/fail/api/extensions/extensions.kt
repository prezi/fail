package com.prezi.fail.api.extensions

import com.prezi.fail.api.ScheduledFailure
import com.prezi.fail.api.period.PeriodFactory
import org.joda.time.DateTime
import com.prezi.fail.api.Failure
import com.prezi.fail.api.FailureStatus
import org.joda.time.Interval


fun Failure.setAtMillis(v: Long?) = setAt(v?.div(1000))
fun Failure.getAtMillis() = getAt()?.times(1000)

fun ScheduledFailure.period() = PeriodFactory.build(getPeriod())
fun ScheduledFailure.buildRun() = Failure()
        .setStatus(FailureStatus.FUTURE)!!
        .setScheduledFailure(this)!!
        .setLog("")!!

public fun ScheduledFailure.nextRun(after: DateTime): Failure =
        buildRun().setAtMillis(period().nextRun(after).getMillis() )!!

public fun ScheduledFailure.nextRuns(between: Interval): List<Failure> =
        period().nextRuns(between).map{ buildRun().setAtMillis(it.getMillis()) }
