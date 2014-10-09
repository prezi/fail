package com.prezi.fail.cli

import org.slf4j.LoggerFactory
import com.linkedin.restli.client.RestClient
import com.prezi.fail.api.ScheduledFailureBuilders


public class ActionListJobs() : ActionApiBase() {
    override val logger = LoggerFactory.getLogger(javaClass)!!

    class object {
        val verb = "list-jobs"
        val cmdLineSyntax = verb
    }

    override public fun run() {
        val request = ScheduledFailureBuilders().getAll()!!.build()!!

        logger.info("Requesting all scheduled jobs")
        withClient {
            it.sendRequest(request)?.getResponseEntity()?.getElements()?.forEach {
                println(it)
            }
        }
    }
}
