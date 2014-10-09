package com.prezi.fail.cli

import com.linkedin.restli.client.RestClient
import com.prezi.fail.api.ScheduledFailureBuilders

import org.slf4j.LoggerFactory


public class ActionList : ActionApiBase() {
    val logger = LoggerFactory.getLogger(javaClass)!!

    class object {
        val verb = "list-jobs"
        val cmdLineSyntax = "${verb} [list-jobs-options]"
    }

    override fun doApiCallAndProcessResponse(client: RestClient) {
        val request = ScheduledFailureBuilders().findByTime()!!
            .beforeParam(config.getListBefore())!!
            .afterParam(config.getListAfter())!!
            .contextParam(config.getListContext())!!
            .atParam(config.getListAt())!!
            .build()!!

        logger.info("Requesting scheduled jobs: ${request.getQueryParamsObjects()}")
        client.sendRequest(request)?.getResponseEntity()?.getElements()?.forEach {
            println(it)
        }
    }
}
