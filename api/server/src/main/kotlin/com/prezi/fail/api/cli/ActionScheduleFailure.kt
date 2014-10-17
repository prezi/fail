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
import com.prezi.fail.api.db.DBCharge
import com.prezi.fail.api.extensions.nextRun

public class ActionScheduleFailure(val args: Array<String>, val systemProperties: StringMap) : Action() {
    val logger = LoggerFactory.getLogger(javaClass)!!

    class object {
        val requiredArgCount = 4
        val verb = "schedule"
        val cmdLineSyntax = "${verb} period tag sapper duration-seconds [sapper-arg ...]"
    }

    override public fun run() {
        val config = ApiCliConfig()
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
                .setScheduledBy(System.getenv("USER"))!!
                .setScheduledAt(System.currentTimeMillis() / 1000)!!
                .setConfiguration(systemProperties)
        logger.info("Scheduling failure: ${scheduledFailure.toString()}")

        if (config.isDryRun()) {
            logger.info("Except I'm not, since this is a dry-run.")
        } else {
            val dbScheduledFailure = DBScheduledFailure(scheduledFailure)
            DB.mapper.save(dbScheduledFailure)

            val firstRun = DBCharge(scheduledFailure.nextRun(DateTime.now()))
            DB.mapper.save(firstRun)

            logger.info("Scheduled ${dbScheduledFailure.id}${dbScheduledFailure.model}, first run will be at ${DateTime(firstRun.getAtMillis())}: ${firstRun.id}${firstRun.model}")
        }
    }
}