package com.prezi.fail.api.cli

import com.linkedin.data.template.StringMap
import com.prezi.fail.cli.Action
import org.slf4j.LoggerFactory


public class ActionList(val regex: String, val systemProperties: StringMap) : Action {
    val logger = LoggerFactory.getLogger(javaClass)!!

    class object {
        val verb = "list"
        val cmdLineSyntax = "${verb} regex"
    }

    override public fun run() {
        val cliConfig = ApiCliConfig()
        cliConfig.configMap = systemProperties
        logger.info("Requesting scheduled charges: regex=${regex} before=${cliConfig.getListBefore()} after=${cliConfig.getListAfter()}")
    }
}
