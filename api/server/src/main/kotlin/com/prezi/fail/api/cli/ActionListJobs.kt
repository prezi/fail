package com.prezi.fail.api.cli

import com.prezi.fail.api.ScheduledFailure


public class ActionListJobs() : LoggingApiAction() {
    class object {
        val verb = "list-jobs"
        val cmdLineSyntax = verb
    }

    override public fun run() {
        logger.info("Listing all scheduled jobs")
        logger.info(ScheduledFailure().setSapper("example1")!!.toString())
        logger.info(ScheduledFailure().setSapper("example2")!!.toString())
    }
}
