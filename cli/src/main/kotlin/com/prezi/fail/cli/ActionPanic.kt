package com.prezi.fail.cli


import com.linkedin.restli.client.RestClient
import com.prezi.fail.api.HealthcheckBuilders
import com.prezi.fail.api.ScheduledFailureBuilders
import org.slf4j.LoggerFactory


public class ActionPanic : ActionApiBase() {
    override val logger = LoggerFactory.getLogger(javaClass)!!

    class object {
        val verb = "panic"
        val cmdLineSyntax = verb
    }

    override public fun run() {
        logger.info("Panicking. Telling the API to cancel all running charges and pause any further schedules.")
        if (config.isDryRun()) {
            logger.info("Except I'm not, since this is a dry-run.")
        } else {
            withClient { client ->
                logger.info(
                        client.sendRequest(ScheduledFailureBuilders().actionPanic())!!.getResponseEntity().toString()
                )
            }
        }
    }
}
