package com.prezi.fail.api.cli

import org.slf4j.LoggerFactory
import com.prezi.fail.cli.Action
import com.linkedin.data.template.StringMap
import com.prezi.fail.api.impl.RunResource
import org.joda.time.Period
import dnl.utils.text.table.TextTable
import com.prezi.fail.api.extensions.getAtMillis
import org.joda.time.format.DateTimeFormat
import com.prezi.fail.api.extensions.toStringTable
import com.prezi.fail.api.extensions.copyToArrayWithoutTheMessedUpArrayStoreException
import com.prezi.fail.config.FailConfig


public class ActionListRuns(systemProperties: StringMap) : Action() {
    val logger = LoggerFactory.getLogger(javaClass)!!
    val config = FailConfig().withConfigMap(systemProperties) as FailConfig
    val dateTimeFormat = DateTimeFormat.forPattern(FailConfig().getDatetimeFormat())

    class object {
        val verb = "list-runs"
        val cmdLineSyntax = verb
    }

    protected fun strToSeconds(s: String?): Int? =
        if (s == null) {
            null
        } else {
            Period(s).toStandardSeconds().getSeconds()
        }

    override public fun run() {
        logger.info(
                TextTable(
                        array("Id", "At", "Sapper", "Target", "Status", "Duration (s)"),
                        RunResource().listRunsByTime(
                                atTimestamp=config.getListAt(),
                                secondsBefore=strToSeconds(config.getListBefore()),
                                secondsAfter=strToSeconds(config.getListAfter()),
                                secondsContext=strToSeconds(config.getListContext())
                        ).map{
                            array<String>(
                                    it.getId(),
                                    dateTimeFormat.print(it.getAtMillis()!!), it.getScheduledFailure().getSapper(),
                                    it.getScheduledFailure().getSearchTerm(), it.getStatus().toString(),
                                    it.getScheduledFailure().getDuration()!!.toString())
                        }.copyToArrayWithoutTheMessedUpArrayStoreException()
                ).toStringTable()
        )
    }
}
