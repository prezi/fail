package com.prezi.fail.api.cli

import com.prezi.fail.api.ScheduledFailure
import org.slf4j.LoggerFactory
import com.prezi.fail.cli.Action
import com.linkedin.data.template.StringMap
import com.prezi.fail.api.impl.FailureResource
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


public class ActionListRuns(systemProperties: StringMap) : Action() {
    val logger = LoggerFactory.getLogger(javaClass)!!
    val config = ApiCliConfig().withConfigMap(systemProperties) as ApiCliConfig
    val dateTimeFormat = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss z")

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

    protected fun copyListToArrayWithoutTheMessedUpArrayStoreException(input: List<Array<String>>): Array<Array<String>> {
        val head = input.head
        if (head == null) {
            return array()
        }
        return Array(input.size, {input[it]})
    }

    override public fun run() {
        val t = TextTable(
                array("At", "Sapper", "Target", "Duration (s)"),
                copyListToArrayWithoutTheMessedUpArrayStoreException(
                    FailureResource().listFailuresByTime(
                            atTimestamp=config.getListAt(),
                            secondsBefore=strToSeconds(config.getListBefore()),
                            secondsAfter=strToSeconds(config.getListAfter()),
                            secondsContext=strToSeconds(config.getListContext())
                    ).map{
                        array<String>(dateTimeFormat.print(it.getAtMillis()!!), it.getScheduledFailure().getSapper(),
                               it.getScheduledFailure().getSearchTerm(), it.getScheduledFailure().getDuration()!!.toString())
                    }
                )
        )
        val baos = ByteArrayOutputStream()
        t.printTable(PrintStream(baos), 0)
        logger.info(baos.toString())
    }
}
