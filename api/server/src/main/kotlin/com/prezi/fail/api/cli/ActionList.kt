package com.prezi.fail.api.cli

import com.linkedin.data.template.StringMap
import com.prezi.fail.cli.Action
import org.slf4j.LoggerFactory
import org.joda.time.format.DateTimeFormat
import com.prezi.fail.api.db.DB
import java.util.regex.Pattern
import dnl.utils.text.table.TextTable
import com.prezi.fail.api.extensions.toStringTable
import com.prezi.fail.api.extensions.copyToArrayWithoutTheMessedUpArrayStoreException
import com.prezi.fail.config.FailConfig


public class ActionList(val regexStr: String, val systemProperties: StringMap) : Action() {
    val logger = LoggerFactory.getLogger(javaClass)!!

    class object {
        val verb = "list"
        val cmdLineSyntax = "${verb} regex"
    }

    override public fun run() {
        val cliConfig = FailConfig()
        val regex = Pattern.compile(regexStr)
        cliConfig.configMap = systemProperties
        val dateTimeFormat = DateTimeFormat.forPattern(FailConfig().getDatetimeFormat())
        logger.debug("Requesting scheduled failures: regex=${regex}")

        logger.info(
            TextTable(
                    array("Id", "Period", "Sapper", "Target", "Duration (s)", "Scheduled by", "Scheduled at"),
                    DB.loadAllScheduledFailures().filter{
                        regex.matcher(it.getSapper()).matches() || regex.matcher(it.getSearchTerm()).matches()
                    }.map{
                        array(it.id!!, it.getPeriod()!!, it.getSapper()!!, it.getSearchTerm()!!, it.getDuration().toString(),
                              it.getScheduledBy()!!, dateTimeFormat.print(it.getScheduledAt()!! * 1000))
                    }.copyToArrayWithoutTheMessedUpArrayStoreException()
            ).toStringTable()
        )
    }
}
