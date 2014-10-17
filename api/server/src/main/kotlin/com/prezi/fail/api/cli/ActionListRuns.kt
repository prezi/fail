package com.prezi.fail.api.cli

import com.prezi.fail.api.ScheduledFailure
import org.slf4j.LoggerFactory
import com.prezi.fail.cli.Action
import com.linkedin.data.template.StringMap
import com.prezi.fail.api.impl.RunResource
import com.linkedin.restli.server.PagingContext
import org.joda.time.Period
import org.joda.time.DateTimeConstants
import org.joda.time.DateTime
import com.prezi.fail.api.db.DB
import dnl.utils.text.table.TextTable
import com.prezi.fail.api.extensions.getAtMillis
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import org.joda.time.format.DateTimeFormat
import com.prezi.fail.api.extensions.toStringTable
import com.prezi.fail.api.extensions.copyToArrayWithoutTheMessedUpArrayStoreException


public class ActionListRuns(systemProperties: StringMap) : Action() {
    val logger = LoggerFactory.getLogger(javaClass)!!
    val config = ApiCliConfig().withConfigMap(systemProperties) as ApiCliConfig
    val dateTimeFormat = DateTimeFormat.forPattern(ApiCliConfig().getDatetimeFormat())

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
                        array("At", "Sapper", "Target", "Duration (s)"),
                        RunResource().listRunsByTime(
                                atTimestamp=config.getListAt(),
                                secondsBefore=strToSeconds(config.getListBefore()),
                                secondsAfter=strToSeconds(config.getListAfter()),
                                secondsContext=strToSeconds(config.getListContext())
                        ).map{
                            array<String>(dateTimeFormat.print(it.getAtMillis()!!), it.getScheduledFailure().getSapper(),
                                    it.getScheduledFailure().getSearchTerm(), it.getScheduledFailure().getDuration()!!.toString())
                        }.copyToArrayWithoutTheMessedUpArrayStoreException()
                ).toStringTable()
        )
    }
}
