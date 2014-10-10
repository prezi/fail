package com.prezi.fail.api.cli

import org.slf4j.LoggerFactory


public class CliActionLogger<T>(loggerFor: Class<T>) {
    val logger = LoggerFactory.getLogger(loggerFor)!!
    var buffer: List<String> = listOf()

    public fun info(msg: String) {
        logger.info(msg)
        buffer += msg
    }
}
