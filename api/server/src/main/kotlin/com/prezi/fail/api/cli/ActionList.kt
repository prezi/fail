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
import com.prezi.fail.api.db.Flag


public class ActionList(val regexStr: String, val systemProperties: StringMap) : Action() {
    val logger = LoggerFactory.getLogger(javaClass)!!

    class object {
        val verb = "list"
        val cmdLineSyntax = "${verb} regex"
    }

    override public fun run() {
        val db = DB()
        val cliConfig = FailConfig()
        val regex = Pattern.compile(regexStr)
        if (Flag.PANIC.get(db.mapper)) {
            logger.warn("NOTE: Panic mode is engaged, no runs are being scheduled.")
        }
        cliConfig.configMap = systemProperties
        logger.debug("Requesting scheduled failures: regex=${regex}")
        logger.info(
                db.loadAllScheduledFailures()
                    .filter { regex.matcher(it.getSapper()).matches() || regex.matcher(it.getSearchTerm()).matches() }
                    .toStringTable())
    }
}
