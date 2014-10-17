package com.prezi.fail.api.cli

import com.prezi.fail.api.ScheduledFailure
import org.slf4j.LoggerFactory
import com.prezi.fail.cli.Action
import com.linkedin.data.template.StringMap
import com.prezi.fail.api.impl.ChargeResource
import com.linkedin.restli.server.PagingContext
import org.joda.time.Period
import org.joda.time.DateTimeConstants
import org.joda.time.DateTime
import com.prezi.fail.api.db.DB


public class ActionListRuns(systemProperties: StringMap) : Action() {
    val logger = LoggerFactory.getLogger(javaClass)!!
    val config = ApiCliConfig().withConfigMap(systemProperties) as ApiCliConfig

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
        logger.info("Listing all scheduled runs")
        ChargeResource().listChargesByTime(
                paging=PagingContext(0, 1000),
                atTimestamp=config.getListAt(),
                secondsBefore=strToSeconds(config.getListBefore()),
                secondsAfter=strToSeconds(config.getListAfter()),
                secondsContext=strToSeconds(config.getListContext())
        ).forEach {
            logger.info("${DateTime(it.getAt() * DateTimeConstants.MILLIS_PER_SECOND)}: ${it}")
        }
    }
}
