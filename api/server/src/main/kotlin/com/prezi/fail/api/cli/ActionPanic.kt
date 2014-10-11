package com.prezi.fail.api.cli

import org.slf4j.LoggerFactory
import com.prezi.fail.cli.Action


public class ActionPanic: Action() {
    val logger = LoggerFactory.getLogger(javaClass)!!

    class object {
        val verb = "panic"
        val cmdLineSyntax = verb
    }

    override fun run() {
        logger.info("PANIC!")
    }
}