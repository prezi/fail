package com.prezi.fail.api.cli

import com.prezi.fail.api.ScheduledFailure
import com.linkedin.data.template.StringArray
import com.linkedin.data.template.StringMap
import com.prezi.fail.cli.Action
import org.slf4j.LoggerFactory
import com.prezi.fail.api.period.PeriodFactory
import com.prezi.fail.api.db.DBScheduledFailure
import com.prezi.fail.api.db.DB
import org.joda.time.DateTime
import com.prezi.fail.api.db.DBRun
import com.prezi.fail.api.extensions.nextRun
import com.prezi.fail.config.FailConfig
import org.joda.time.DateTimeConstants
import com.prezi.fail.api.extensions.toStringTable
import com.prezi.fail.api.db.Flag

public class ActionScheduleFailure(val args: Array<String>, val systemProperties: StringMap) : Action() {
    val logger = LoggerFactory.getLogger(javaClass)!!

    class object {
        val requiredArgCount = 4
        val verb = "schedule"
        val cmdLineSyntax = "${verb} period tag sapper duration-seconds [sapper-arg ...]"
    }

    override public fun run() {
        val config = FailConfig()
        config.configMap = systemProperties

        val period = args[0]
        val searchTerm = args[1]
        val sapper = args[2]
        val duration = args[3].toInt()

        // Verify the period definition is correct
        try {
            PeriodFactory.build(period)
        } catch(e: PeriodFactory.InvalidPeriodDefinition) {
            logger.error("Can't understand period definition \"${period}\"")
            exitCode = 1
            return
        }

        val scheduledFailure = ScheduledFailure()
                .setPeriod(period)!!
                .setSearchTerm(searchTerm)!!
                .setSapper(sapper)!!
                .setDuration(duration)!!
                .setSapperArgs(StringArray(args.drop(ActionScheduleFailure.requiredArgCount)))!!
                .setScheduledBy(systemProperties.getOrElse("user.name", {"unknown"}))!!
                .setScheduledAt(DateTime.now().getMillis() / DateTimeConstants.MILLIS_PER_SECOND)!!
                .setConfiguration(systemProperties)
        logger.info("Failure to schedule:\n${scheduledFailure.toStringTable()}")

        if (config.isDryRun()) {
            logger.info("Except I'm not, since this is a dry-run.")
        } else {
            val db = DB()

            val dbScheduledFailure = DBScheduledFailure(scheduledFailure)
            db.mapper.save(dbScheduledFailure)

            if (Flag.PANIC.get(db.mapper)) {
                logger.warn("NOTE: Panic mode is engaged, no runs are being scheduled.")
                logger.info("Scheduled failure with ID ${dbScheduledFailure.getId()}")
            } else {
                val firstRun = DBRun(scheduledFailure.nextRun(DateTime.now())).setScheduledFailure(dbScheduledFailure)!!
                db.mapper.save(firstRun)

                logger.info("Scheduled failure with ID ${dbScheduledFailure.getId()}, first run will be at ${DateTime(firstRun.getAtMillis())}")
                logger.debug("First run details: ${firstRun.model}")
            }
        }
    }
}