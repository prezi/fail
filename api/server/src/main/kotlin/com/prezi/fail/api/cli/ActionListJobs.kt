package com.prezi.fail.api.cli

import com.prezi.fail.api.ScheduledFailure
import org.slf4j.LoggerFactory
import com.prezi.fail.cli.Action


public class ActionListJobs() : Action {
    val logger = LoggerFactory.getLogger(javaClass)!!

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
