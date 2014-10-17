package com.prezi.fail.api.extensions

import com.prezi.fail.api.ScheduledFailure
import com.prezi.fail.api.period.PeriodFactory
import org.joda.time.DateTime
import com.prezi.fail.api.Run
import com.prezi.fail.api.RunStatus
import org.joda.time.Interval


fun Run.setAtMillis(v: Long?) = setAt(v?.div(1000))
fun Run.getAtMillis() = getAt()?.times(1000)

fun ScheduledFailure.period() = PeriodFactory.build(getPeriod())
fun ScheduledFailure.buildRun() = Run()
        .setStatus(RunStatus.FUTURE)!!
        .setScheduledFailure(this)!!
        .setLog("")!!

public fun ScheduledFailure.nextRun(after: DateTime): Run =
        buildRun().setAtMillis(period().nextRun(after).getMillis() )!!

public fun ScheduledFailure.nextRuns(between: Interval): List<Run> =
        period().nextRuns(between).map{ buildRun().setAtMillis(it.getMillis()) }
