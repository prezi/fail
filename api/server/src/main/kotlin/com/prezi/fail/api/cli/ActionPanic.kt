package com.prezi.fail.api.cli


public class ActionPanic: LoggingApiAction() {
    class object {
        val verb = "panic"
        val cmdLineSyntax = verb
    }

    override fun run() {
        logger.info("PANIC!")
    }
}