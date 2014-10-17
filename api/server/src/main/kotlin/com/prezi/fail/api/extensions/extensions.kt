package com.prezi.fail.api.extensions

import com.prezi.fail.api.ScheduledFailure
import com.prezi.fail.api.period.PeriodFactory
import org.joda.time.DateTime
import com.prezi.fail.api.Charge
import com.prezi.fail.api.ChargeStatus
import org.joda.time.Interval


fun Charge.setAtMillis(v: Long?) = setAt(v?.div(1000))
fun Charge.getAtMillis() = getAt()?.times(1000)

fun ScheduledFailure.period() = PeriodFactory.build(getPeriod())
fun ScheduledFailure.buildRun() = Charge()
        .setStatus(ChargeStatus.FUTURE)!!
        .setScheduledFailure(this)!!
        .setLog("")!!

public fun ScheduledFailure.nextRun(after: DateTime): Charge =
        buildRun().setAtMillis(period().nextRun(after).getMillis() )!!

public fun ScheduledFailure.nextRuns(between: Interval): List<Charge> =
        period().nextRuns(between).map{ buildRun().setAtMillis(it.getMillis()) }
