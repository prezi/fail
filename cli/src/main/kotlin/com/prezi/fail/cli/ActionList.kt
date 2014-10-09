package com.prezi.fail.cli

import com.linkedin.restli.client.RestClient
import com.prezi.fail.api.ChargeBuilders

import org.slf4j.LoggerFactory


public class ActionList(val regex: String) : ActionApiBase() {
    override val logger = LoggerFactory.getLogger(javaClass)!!

    class object {
        val verb = "list"
        val cmdLineSyntax = "${verb} regex"
    }

    override fun run() {
        val request = ChargeBuilders().findByTimeAndRegex()!!
            .regexParam(regex)!!
            .beforeParam(config.getListBefore())!!
            .afterParam(config.getListAfter())!!
            .contextParam(config.getListContext())!!
            .atParam(config.getListAt())!!
            .build()!!

        logger.info("Requesting scheduled charges: ${request.getQueryParamsObjects()}")
        withClient {
            it.sendRequest(request)?.getResponseEntity()?.getElements()?.forEach {
                println(it)
            }
        }
    }
}
