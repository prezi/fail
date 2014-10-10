package com.prezi.fail.api.cli

import com.prezi.fail.api.ScheduledFailure
import com.linkedin.data.template.StringArray
import com.linkedin.data.template.StringMap
import com.prezi.fail.cli.Action
import org.slf4j.LoggerFactory

public class ActionScheduleFailure(val args: Array<String>, val systemProperties: StringMap) : Action {
    val logger = LoggerFactory.getLogger(javaClass)!!

    class object {
        val requiredArgCount = 4
        val verb = "schedule"
        val cmdLineSyntax = "${verb} interval tag sapper duration-seconds [sapper-arg ...]"
    }

    override public fun run() {
        logger.info("Note: this is just a placeholder for now, no failures are actually being scheduled.")
        val config = ApiCliConfig()
        config.configMap = systemProperties

        val interval = args[0]
        val searchTerm = args[1]
        val sapper = args[2]
        val duration = args[3].toInt()

        val scheduledFailure = ScheduledFailure()
                .setInterval(interval)!!
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
            logger.info("This is where I'd schedule ${scheduledFailure.toString()}")
        }
    }
}