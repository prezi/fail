package com.prezi.fail.cli

import org.slf4j.LoggerFactory
import com.linkedin.restli.client.RestClient
import com.prezi.fail.api.ScheduledFailureBuilders


public class ActionListJobs() : ActionApiBase() {
    val logger = LoggerFactory.getLogger(javaClass)!!

    class object {
        val verb = "list"
        val cmdLineSyntax = "${verb} [list-jobs-options]"
    }

    override fun doApiCallAndProcessResponse(client: RestClient) {
        val request = ScheduledFailureBuilders().getAll()!!.build()!!

        logger.info("Requesting all scheduled jobs")
        client.sendRequest(request)?.getResponseEntity()?.getElements()?.forEach {
            println(it)
        }
    }
}
