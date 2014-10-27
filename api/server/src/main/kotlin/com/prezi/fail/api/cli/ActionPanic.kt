package com.prezi.fail.api.cli

import org.slf4j.LoggerFactory
import com.prezi.fail.cli.Action
import com.prezi.fail.api.db.Flag
import com.prezi.fail.api.db.DB
import com.prezi.fail.api.impl.RunResource
import org.joda.time.DateTime
import com.prezi.fail.api.RunStatus
import com.prezi.fail.api.queue.Queue


public class ActionPanic(val db: DB = DB()): Action() {
    val logger = LoggerFactory.getLogger(javaClass)!!


    class object {
        val verb = "panic"
        val cmdLineSyntax = verb
    }

    override fun run() {
        if (Flag.PANIC.get(db.mapper)) {
            logger.warn("We're already in panic mode.")
        } else {
            Flag.PANIC.set(db.mapper, true)
            Queue().putPoisonPill()
            logger.info("Panic engaged. Running injections are aborted, scheduled runs are cancelled, no future runs are being scheduled.")
        }
    }
}