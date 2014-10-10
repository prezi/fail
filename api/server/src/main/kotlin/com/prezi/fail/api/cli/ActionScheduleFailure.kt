package com.prezi.fail.api.cli

import com.prezi.fail.api.ScheduledFailure
import com.linkedin.data.template.StringArray
import com.linkedin.data.template.StringMap

public class ActionScheduleFailure(val args: Array<String>, val systemProperties: StringMap) : LoggingApiAction() {
    class object {
        val requiredArgCount = 4
        val verb = "schedule"
        val cmdLineSyntax = "${verb} interval tag sapper duration-seconds [sapper-arg ...]"
    }

    override public fun run() {
        println("Note: this is just a placeholder for now, no failures are actually being scheduled.")
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

        // TODO: config reading should be more general
        if (systemProperties.get(ApiCliConfigKey.DRY_RUN.key) == "true") {
            logger.info("Except I'm not, since this is a dry-run.")
        } else {
            logger.info("This is where I'd schedule ${scheduledFailure.toString()}")
        }
    }
}