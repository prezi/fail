package com.prezi.fail.api.extensions

import com.prezi.fail.api.ScheduledFailure
import com.prezi.fail.api.period.PeriodFactory
import org.joda.time.DateTime
import com.prezi.fail.api.Run
import com.prezi.fail.api.RunStatus
import org.joda.time.Interval
import dnl.utils.text.table.TextTable
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import com.prezi.fail.api.db.DBScheduledFailure
import org.joda.time.format.DateTimeFormat
import com.prezi.fail.config.FailConfig


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

public fun List<Array<String>>.copyToArrayWithoutTheMessedUpArrayStoreException(): Array<Array<String>> {
    val head = head
    if (head == null) {
        return array()
    }
    return Array(size, {this[it]})
}

public fun TextTable.toStringTable(): String {
    val baos = ByteArrayOutputStream()
    printTable(PrintStream(baos), 0)
    return baos.toString()
}

public fun List<DBScheduledFailure>.toStringTable(): String =
        TextTable(
                array("Id", "Period", "Sapper", "Target", "Duration (s)", "Scheduled by", "Scheduled at"),
                map{
                    array(it.getId()!!, it.getPeriod()!!, it.getSapper()!!, it.getSearchTerm()!!, it.getDuration().toString(),
                            it.getScheduledBy()!!, DateTimeFormat.forPattern(FailConfig().getDatetimeFormat()).print(it.getScheduledAt()!! * 1000))
                }.copyToArrayWithoutTheMessedUpArrayStoreException()
        ).toStringTable()

public fun ScheduledFailure.toStringTable(): String =
        TextTable(
                array("Period", "Sapper", "Target", "Duration (s)", "Scheduled by", "Scheduled at"),
                array(
                    array(getPeriod()!!, getSapper()!!, getSearchTerm()!!, getDuration().toString(),
                            getScheduledBy()!!, DateTimeFormat.forPattern(FailConfig().getDatetimeFormat()).print(getScheduledAt()!! * 1000)))
        ).toStringTable()
