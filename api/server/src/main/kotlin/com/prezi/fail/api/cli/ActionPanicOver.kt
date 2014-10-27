package com.prezi.fail.api.cli

import org.slf4j.LoggerFactory
import com.prezi.fail.cli.Action
import com.prezi.fail.api.db.Flag
import com.prezi.fail.api.db.DB


public class ActionPanicOver(val db: DB = DB()): Action() {
    val logger = LoggerFactory.getLogger(javaClass)!!


    class object {
        val verb = "panic-over"
        val cmdLineSyntax = verb
    }

    override fun run() {
        if (Flag.PANIC.get(db.mapper)) {
            Flag.PANIC.set(db.mapper, false)
            logger.info("Panic is over, continuing scheduling.")
        } else {
            logger.warn("We're not in panic mode.")
        }
    }
}